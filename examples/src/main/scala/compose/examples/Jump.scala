package compose.examples

import compose.core._
import compose.core.Score._
import compose.core.Pitch._
import compose.core.{Rest => r}
import scalajs.js.annotation.JSExport

trait Jump {

  val beat = C4.s ~ r.s

  val countIn =
    beat ~
    beat ~
    beat ~
    beat

  val riff =
    beat ~
    beat ~
    (G4.e | D5.e) ~
    beat ~
    beat ~
    (G4.e | C5.e | E5.e) ~
    beat ~
    beat ~
    (F4.e | A4.e | C5.e) ~
    beat ~
    beat ~
    (F4.e | A4.e | C5.e) ~
    beat ~
    (G4.e | D5.e) ~
    beat ~
    (G4.q.dotted | D5.q.dotted) ~
    (G4.e | C5.e | E5.e) ~
    beat ~
    beat ~
    (F4.e | A4.e | C5.e) ~
    beat ~
    (C4.q | F4.q | A4.q) ~
    (C4.q | E4.q | G4.q) ~
    (C4.e * 5 | D4.e * 5 | G4.e * 5)

  @JSExport
  val jump = countIn ~ riff ~ riff
}
