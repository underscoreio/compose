package compose.supercollider

import de.sciss.synth._
import de.sciss.synth.ugen._
import de.sciss.synth.Ops._
import scala.annotation.tailrec

sealed trait Command
case class NoteOn(channel: Int, freq: Double) extends Command
case class NoteOff(channel: Int) extends Command
case class Wait(millis: Long) extends Command {
  def >(that: Wait) = this.millis > that.millis
  def <(that: Wait) = this.millis < that.millis
  def +(that: Wait) = Wait(this.millis + that.millis)
  def -(that: Wait) = Wait(this.millis - that.millis)
}

object Commands {
  val empty = Commands(Seq.empty)
}

case class Commands(commands: Seq[Command]) {
  def maxChannel: Int =
    commands.collect {
      case NoteOn(channel, _) => channel
      case NoteOff(channel)   => channel
    } match {
      case Seq() => 0
      case seq   => seq.max
    }

  def renumberChannels: Commands =
    renumberChannels(0)

  def renumberChannels(base: Int): Commands = {
    val original = commands.collect {
      case NoteOn(channel, _) => channel
      case NoteOff(channel)   => channel
    }.distinct.sorted

    val renumber = original.zipWithIndex.map {
      case (a, b) => (a, b + base)
    }.toMap

    Commands(commands map {
      case NoteOn(channel, freq) => NoteOn(renumber(channel), freq)
      case NoteOff(channel)      => NoteOff(renumber(channel))
      case Wait(duration)        => Wait(duration)
    })
  }

  def +:(command: Command) = Commands(command +: commands)
  def :+(command: Command) = Commands(commands :+ command)
  def ++(that: Commands)   = Commands(this.commands ++ that.commands)

  def merge(that: Commands): Commands = {
    @tailrec def loop(a: Seq[Command], b: Seq[Command], accum: Seq[Command] = Seq.empty): Seq[Command] = {
      a match {
        case (aHead: NoteOn) +: aTail =>
          loop(aTail, b, accum :+ aHead)

        case (aHead: NoteOff) +: aTail =>
         loop(aTail, b, accum :+ aHead)

        case (aHead: Wait) +: aTail =>
          b match {
            case (bHead: NoteOn) +: bTail =>
              loop(a, bTail, accum :+ bHead)

            case (bHead: NoteOff) +: bTail =>
              loop(a, bTail, accum :+ bHead)

            case (bHead: Wait) +: bTail if aHead > bHead =>
              loop((aHead - bHead) +: aTail, bTail, accum :+ bHead)

            case (bHead: Wait) +: bTail =>
              loop(aTail, (bHead - aHead) +: bTail, accum :+ aHead)

            case Seq() =>
              accum ++ a
          }

        case Seq() =>
          accum ++ b
      }
    }

    val a = this.renumberChannels
    val b = that.renumberChannels(a.maxChannel + 1)

    Commands(loop(a.commands, b.commands)).renumberChannels
  }

}
