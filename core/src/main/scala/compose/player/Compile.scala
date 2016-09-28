package compose.player

import cats.data.State
import cats.syntax.order._
import compose.core._
import scala.annotation.tailrec

object Compile {
  import Command._

  case class CompilerState(id: Int = 0, instrument: Instrument = Instrument.default)

  def apply(score: Score): Vector[Command] =
    compile(score).runA(CompilerState()).value

  def setInstrument(instrument: Instrument): State[CompilerState, Unit] =
    State.modify(s => s.copy(instrument = instrument))

  private[player] def compile(score: Score): State[CompilerState, Vector[Command]] =
    score match {
      case EmptyScore =>
        State.pure(Vector())

      case SeqScore(a, b) =>
        for {
          a <- compile(a)
          b <- compile(b)
        } yield a ++ b

      case ParScore(a, b) =>
        for {
          a <- compile(a)
          b <- compile(b)
        } yield interleave(a, b)

      case Rest(duration) =>
        State.pure(Vector(Wait(duration)))

      case Note(pitch, duration) =>
        State { s =>
          (
            s.copy(id = s.id + 1),
            Vector(
              createNoteOn(s.id, s.instrument, pitch),
              Wait(duration),
              NoteOff(s.id)
            )
          )
        }

      case Arrange(score, instrument) =>
        for {
          _ <- setInstrument(instrument)
          a <- compile(score)
        } yield a
    }

  private[player] def createNoteOn(id: Int, instrument: Instrument, pitch: Pitch): NoteOn =
    instrument match {
      case Tuned(sample, tuning) => NoteOn(id, sample, pitch transpose tuning)
      case Fixed(sample, fixed)  => NoteOn(id, sample, fixed)
      case Combi(func)           => createNoteOn(id, func(pitch), pitch)
    }

  private[player] def interleave(commands1: Vector[Command], commands2: Vector[Command]): Vector[Command] = {
    @tailrec def loop(a: Vector[Command], b: Vector[Command], accum: Vector[Command]): Vector[Command] = {
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

            case Vector() =>
              accum ++ a
          }

        case Vector() =>
          accum ++ b
      }
    }

    loop(commands1, commands2, Vector.empty)
  }

  private[player] def subtract(a: Wait, b: Wait): Wait = {
    Wait(Duration(a.duration.value - b.duration.value))
  }
}