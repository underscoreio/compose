package compose.examples

import compose.core._
import scalajs.js.annotation.JSExport

trait ChordProgression{
  import Score._
  import Pitch._

  @JSExport
  val chordProgression =
    ( C3.q  | E3.q  | G3.q  ) ~
    ( C3.q  | F3.q  | A3.q  ) ~
    ( D3.q  | F3.q  | As3.q ) ~
    ( Ds3.q | G3.q  | As3.q ) ~
    ( Ds3.q | Gs3.q | C4.q  ) ~
    ( F3.q  | Gs3.q | Cs4.q ) ~
    ( Fs3.q | As3.q | Cs4.q ) ~
    ( Fs3.q | B3.q  | Ds4.q ) ~
    ( Gs3.q | B3.q  | E4.q  ) ~
    ( A3.q  | Cs4.q | E4.q  ) ~
    ( A3.q  | D4.q  | Fs4.q ) ~
    ( B3.q  | D4.q  | G4.q  ) ~
    ( C4.w  | E4.w  | G4.w  )
}
