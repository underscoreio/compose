package compose.examples

import compose.core._
import compose.core.Score._
import compose.core.Pitch._
import compose.core.{Rest => r}
import scalajs.js.annotation.JSExport

trait Jump {

  val countIn =
    C4.e repeat 4

  val bassline =
    C4.e repeat 32

  val riff =
    r.e ~
    r.e ~
    (G4.e | D5.e) ~
    r.e ~
    r.e ~
    (G4.e | C5.e | E5.e) ~
    r.e ~
    r.e ~
    (F4.e | A4.e | C5.e) ~
    r.e ~
    r.e ~
    (F4.e | A4.e | C5.e) ~
    r.e ~
    (G4.e | D5.e) ~
    r.e ~
    (G4.q.dotted | D5.q.dotted) ~
    (G4.e | C5.e | E5.e) ~
    r.e ~
    r.e ~
    (F4.e | A4.e | C5.e) ~
    r.e ~
    (C4.q | F4.q | A4.q) ~
    (C4.q | E4.q | G4.q) ~
    (C4.e * 5 | D4.e * 5 | G4.e * 5)

  @JSExport
  val jump = countIn ~ (riff | bassline) ~ (riff | bassline)
}
