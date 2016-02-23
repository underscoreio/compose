package compose.core

import org.scalatest._

class PitchSpec extends FreeSpec with Matchers {
  import Pitch._

  "A4 should have the correct number and frequency" in {
    A4.value should equal(0)
  }

  "A3 should have the correct number and frequency" in {
    A3.value should equal(-12)
  }

  "A5 should have the correct number and frequency" in {
    A5.value should equal(12)
  }
}
