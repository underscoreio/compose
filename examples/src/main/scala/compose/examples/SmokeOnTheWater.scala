package compose.examples

import compose.core._
import scalajs.js.annotation.JSExport

trait SmokeOnTheWater {
  import Score._
  import Pitch._

  @JSExport
  val smokeOnTheWater = (
    (E4.e ~ Rest.e ~ G4.e ~ Rest.e ~ A4.q.dotted ~ E4.e ~ Rest.e ~ G4.e ~ Rest.e ~ As4.e ~ A4.h) |
    (B3.e ~ Rest.e ~ D4.e ~ Rest.e ~ E4.q.dotted ~ B3.e ~ Rest.e ~ D4.e ~ Rest.e ~ F4.e  ~ E4.h) |
    (E3.e ~ Rest.e ~ G3.e ~ Rest.e ~ A3.q.dotted ~ E3.e ~ Rest.e ~ G3.e ~ Rest.e ~ As3.e ~ A3.h)
  ) ~ (
    (E4.e ~ Rest.e ~ G4.e ~ Rest.e ~ A4.q.dotted ~ G4.e ~ Rest.e ~ E4.h.doubleDotted) |
    (B3.e ~ Rest.e ~ D4.e ~ Rest.e ~ E4.q.dotted ~ D4.e ~ Rest.e ~ B3.h.doubleDotted) |
    (E3.e ~ Rest.e ~ G3.e ~ Rest.e ~ A3.q.dotted ~ G3.e ~ Rest.e ~ E3.h.doubleDotted)
  )
}
