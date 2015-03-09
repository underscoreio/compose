package compose.player

import compose.core._
import scala.annotation.tailrec

object Implicits {
  implicit class IntOps(val num: Int) extends AnyVal {
    def bpm = Tempo(num)
  }

  implicit class NoteOps(val note: Note) extends AnyVal {
    def frequency: Double =
      math.pow(2, note.value / 12.0) * 440
  }

  implicit class ScoreOps(val score: Score) extends AnyVal {
    def compile(implicit tempo: Tempo): Seq[Command] =
      score match {
        case EmptyScore           => Vector()
        case SeqScore(a, b)       => a.compile ++ b.compile
        case ParScore(a, b)       => a.compile merge b.compile
        case RestScore(dur)       => Vector(Wait(tempo.millis(dur)))
        case NoteScore(note, dur) =>
          Vector(
            NoteOn(0, note.frequency),
            Wait(tempo.millis(dur)),
            NoteOff(0)
          )
      }
  }

  implicit class CommandOps(val commands: Seq[Command]) extends AnyVal {
    def toSeq = commands

    def maxChannel: Int =
      commands.collect {
        case NoteOn(channel, _) => channel
        case NoteOff(channel)   => channel
      } match {
        case Seq() => 0
        case seq   => seq.max
      }

    def renumberChannels(base: Int): Seq[Command] = {
      val original = commands.collect {
        case NoteOn(channel, _) => channel
        case NoteOff(channel)   => channel
      }.distinct.sorted

      val renumber = original.zipWithIndex.map {
        case (a, b) => (a, b + base)
      }.toMap

      commands map {
        case NoteOn(channel, freq) => NoteOn(renumber(channel), freq)
        case NoteOff(channel)      => NoteOff(renumber(channel))
        case Wait(duration)        => Wait(duration)
      }
    }

    def merge(that: Seq[Command]): Seq[Command] = {
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

      val a = this.renumberChannels(0)
      val b = that.renumberChannels(a.maxChannel + 1)

      loop(a, b).renumberChannels(0)
    }
  }
}