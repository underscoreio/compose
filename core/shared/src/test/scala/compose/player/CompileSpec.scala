package compose.player

import compose.core._
import org.scalatest._

class CompileSpec extends FreeSpec with Matchers {
  import Command._

  "compiler should number sequential notes" in {
    val score = Score.Seq(
      Score.Seq(
        Score.Note(Pitch.C4, Duration.Half),
        Score.Note(Pitch.E4, Duration.Half)
      ),
      Score.Seq(
        Score.Note(Pitch.G4, Duration.Half),
        Score.Note(Pitch.C5, Duration.Half)
      )
    )

    Compile(score) should equal(Vector(
      NoteOn(0, Pitch.C4),
      Wait(Duration.Half),
      NoteOff(0),
      NoteOn(1, Pitch.E4),
      Wait(Duration.Half),
      NoteOff(1),
      NoteOn(2, Pitch.G4),
      Wait(Duration.Half),
      NoteOff(2),
      NoteOn(3, Pitch.C5),
      Wait(Duration.Half),
      NoteOff(3)
    ))
  }

  "compiler should number parallel notes" in {
    val score = Score.Par(
      Score.Par(
        Score.Note(Pitch.C4, Duration.Half),
        Score.Note(Pitch.E4, Duration.Half)
      ),
      Score.Par(
        Score.Note(Pitch.G4, Duration.Half),
        Score.Note(Pitch.C5, Duration.Half)
      )
    )

    Compile(score) should equal(Vector(
      NoteOn(0, Pitch.C4),
      NoteOn(1, Pitch.E4),
      NoteOn(2, Pitch.G4),
      NoteOn(3, Pitch.C5),
      Wait(Duration.Half),
      NoteOff(0),
      NoteOff(1),
      NoteOff(2),
      NoteOff(3)
    ))
  }

  "compiler should interleave overlapping notes" in {
    val score = Score.Par(
      Score.Seq(
        Score.Rest(Duration.Eighth),
        Score.Seq(
          Score.Note(Pitch.C4, Duration.Half),
          Score.Note(Pitch.E4, Duration.Half)
        )
      ),
      Score.Seq(
        Score.Note(Pitch.G4, Duration.Half),
        Score.Note(Pitch.C5, Duration.Half)
      )
    )

    Compile(score) should equal(Vector(
      NoteOn(2, Pitch.G4),
      Wait(Duration.Eighth),
      NoteOn(0, Pitch.C4),
      Wait(Duration.Eighth * 3),
      NoteOff(2),
      NoteOn(3, Pitch.C5),
      Wait(Duration.Eighth),
      NoteOff(0),
      NoteOn(1, Pitch.E4),
      Wait(Duration.Eighth * 3),
      NoteOff(3),
      Wait(Duration.Eighth),
      NoteOff(1)
    ))
  }
}
