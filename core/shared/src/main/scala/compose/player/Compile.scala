package compose.player

import cats.data.State
import cats.syntax.order._
import compose.core._
import scala.annotation.tailrec

object Compile {
  import Command._

  // Internal compiler state.
  //
  // The integer is the next unique ID to assign
  // to a pair of NoteOn/NoteOff commands.
  type CompilerState[A] = State[Int, A]

  def apply(score: Score): Seq[Command] =
    compile(score).runA(0).value

  private[player] def compile(score: Score): CompilerState[Seq[Command]] =
    score match {
      case Score.Empty            => State.pure(Vector())
      case Score.Seq(a, b)        => for {
                                       a <- compile(a)
                                       b <- compile(b)
                                     } yield a ++ b
      case Score.Par(a, b)        => for {
                                       a <- compile(a)
                                       b <- compile(b)
                                     } yield interleave(a, b)
      case Score.Rest(dur)        => State.pure(Vector(Wait(dur)))
      case Score.Note(pitch, dur) => State.apply(id => (
                                       id + 1,
                                       Vector(
                                         NoteOn(id, pitch),
                                         Wait(dur),
                                         NoteOff(id)
                                       )
                                     ))
    }

  // private[player] def maxChannel(commands: Seq[Command]): Int =
  //   commands.collect {
  //     case NoteOn(channel, _) => channel
  //     case NoteOff(channel)   => channel
  //   } match {
  //     case Seq() => 0
  //     case seq   => seq.max
  //   }

  // private[player] def renumberChannels(commands: Seq[Command], base: Int): Seq[Command] = {
  //   val original = commands.collect {
  //     case NoteOn(channel, _) => channel
  //     case NoteOff(channel)   => channel
  //   }.distinct.sorted

  //   val renumber = original.zipWithIndex.map {
  //     case (a, b) => (a, b + base)
  //   }.toMap

  //   commands map {
  //     case NoteOn(channel, freq) => NoteOn(renumber(channel), freq)
  //     case NoteOff(channel)      => NoteOff(renumber(channel))
  //     case Wait(duration)        => Wait(duration)
  //   }
  // }

  private[player] def interleave(commands1: Seq[Command], commands2: Seq[Command]): Seq[Command] = {
    @tailrec def loop(a: Seq[Command], b: Seq[Command], accum: Seq[Command]): Seq[Command] = {
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
              val head = subtract(aHead, bHead)

              if(head.duration.value == 0) {
                loop(aTail, bTail, accum :+ bHead)
              } else {
                loop(head +: aTail, bTail, accum :+ bHead)
              }

            case (bHead: Wait) +: bTail =>
              val head = subtract(bHead, aHead)

              if(head.duration.value == 0) {
                loop(aTail, bTail, accum :+ bHead)
              } else {
                loop(aTail, head +: bTail, accum :+ aHead)
              }

            case Seq() =>
              accum ++ a
          }

        case Seq() =>
          accum ++ b
      }
    }

    loop(commands1, commands2, Vector.empty)
  }

  private[player] def subtract(a: Wait, b: Wait): Wait = {
    Wait(Duration(a.duration.value - b.duration.value))
  }
}