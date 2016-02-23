package compose.core

import org.scalatest._

class PitchSpec extends FlatSpec with Matchers {
  "A4" should "have the correct number and frequency" in {
    Pitch.A4.frequency should equal(440.0)
  }

  "A3" should "have the correct number and frequency" in {
    Pitch.A3.frequency should equal(220.0)
  }

  "A5" should "have the correct number and frequency" in {
    Pitch.A5.frequency should equal(880.0)
  }
}
