package compose.supercollider

import org.scalatest._

class PlayerSpec extends FlatSpec with Matchers {
  import compose.core.Note._

  "A4" should "have the correct number and frequency" in {
    Player.frequency(A4) should equal(440)
  }

  "A3" should "have the correct number and frequency" in {
    Player.frequency(A3) should equal(220)
  }

  "A5" should "have the correct number and frequency" in {
    Player.frequency(A5) should equal(880)
  }
}
