package compose.tablature

import compose.core._
import compose.core.Score.{Rest => r}
import org.scalatest._

class TablatureSyntaxSpec extends FreeSpec with Matchers {
  import Pitch._

  "simple tab should compile correctly" in {
    val openPitchs =
      tab"""
      0
      0
      0
      0
      0
      0
      """
    openPitchs should equal(
      E5.s |
      B4.s |
      G4.s |
      D4.s |
      A3.s |
      E3.s
    )
  }

  "smoke on the water should compile correctly" in {
    val smokeOnTheWater =
      tab"""
      # Smoke on the Water

      --------------------------------
      --------------------------------
      --------------------------------
      2-x-5-x-7-----2-x-5-x-8-7-------
      2-x-5-x-7-----2-x-5-x-8-7-------
      0-x-3-x-5-----0-x-3-x-6-5-------

      --------------------------------
      --------------------------------
      --------------------------------
      2-x-5-x-7-----5---2-------------
      2-x-5-x-7-----5---2-------------
      0-x-3-x-5-----3---0-------------
      """
    smokeOnTheWater should equal(
      (
        (r.w) |
        (r.w) |
        (r.w) |
        (E4.s ~ r.s ~ G4.s ~ r.s ~ A4.e.dotted ~ E4.s ~ r.s ~ G4.s ~ r.s ~ As4.s ~ A4.q) |
        (B3.s ~ r.s ~ D4.s ~ r.s ~ E4.e.dotted ~ B3.s ~ r.s ~ D4.s ~ r.s ~ F4.s  ~ E4.q) |
        (E3.s ~ r.s ~ G3.s ~ r.s ~ A3.e.dotted ~ E3.s ~ r.s ~ G3.s ~ r.s ~ As3.s ~ A3.q)
      ).halfTime ~
      (
        (r.w) |
        (r.w) |
        (r.w) |
        (E4.s ~ r.s ~ G4.s ~ r.s ~ A4.e.dotted ~ G4.e ~ E4.q.doubleDotted) |
        (B3.s ~ r.s ~ D4.s ~ r.s ~ E4.e.dotted ~ D4.e ~ B3.q.doubleDotted) |
        (E3.s ~ r.s ~ G3.s ~ r.s ~ A3.e.dotted ~ G3.e ~ E3.q.doubleDotted)
      ).halfTime
    )
  }
}
