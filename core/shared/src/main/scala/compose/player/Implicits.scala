package compose.player

import compose.core._
import scala.annotation.tailrec

object Implicits {
  implicit class IntOps(val num: Int) extends AnyVal {
    def bpm = Tempo(num)
  }

  implicit class PitchOps(val pitch: Pitch) extends AnyVal {
    def frequency: Double =
      math.pow(2, pitch.value / 12.0) * 440
  }

  implicit class ScoreOps(val score: Score) extends AnyVal {
    def compile(implicit tempo: Tempo): Seq[Command] =
      score match {
        case Score.Empty     => Vector()
        case Score.Seq(a, b) => a.compile ++ b.compile
        case Score.Par(a, b) => a.compile merge b.compile
        case Score.Rest(dur) => Vector(Wait(tempo.millis(dur)))
        case Score.Note(pitch, dur) =>
          Vector(
            PitchOn(0, pitch.frequency),
            Wait(tempo.millis(dur)),
            PitchOff(0)
          )
      }
  }

  implicit class CommandOps(val commands: Seq[Command]) extends AnyVal {
    def toSeq = commands

    def maxChannel: Int =
      commands.collect {
        case PitchOn(channel, _) => channel
        case PitchOff(channel)   => channel
      } match {
        case Seq() => 0
        case seq   => seq.max
      }

    def renumberChannels(base: Int): Seq[Command] = {
      val original = commands.collect {
        case PitchOn(channel, _) => channel
        case PitchOff(channel)   => channel
      }.distinct.sorted

      val renumber = original.zipWithIndex.map {
        case (a, b) => (a, b + base)
      }.toMap

      commands map {
        case PitchOn(channel, freq) => PitchOn(renumber(channel), freq)
        case PitchOff(channel)      => PitchOff(renumber(channel))
        case Wait(duration)        => Wait(duration)
      }
    }

    def merge(that: Seq[Command]): Seq[Command] = {
      @tailrec def loop(a: Seq[Command], b: Seq[Command], accum: Seq[Command] = Seq.empty): Seq[Command] = {
        a match {
          case (aHead: PitchOn) +: aTail =>
            loop(aTail, b, accum :+ aHead)

          case (aHead: PitchOff) +: aTail =>
           loop(aTail, b, accum :+ aHead)

          case (aHead: Wait) +: aTail =>
            b match {
              case (bHead: PitchOn) +: bTail =>
                loop(a, bTail, accum :+ bHead)

              case (bHead: PitchOff) +: bTail =>
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