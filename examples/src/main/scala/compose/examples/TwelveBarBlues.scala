package compose.examples

import compose.core._

trait TwelveBarBlues {
  import Score._
  import Pitch._

  def twelveBarBlues = {
    val bar =
      ( E3.q | B3.q  ) ~
      ( E3.s | B3.s  ) ~
      ( E3.q | Cs4.q ) ~
      ( E3.s | B3.s  )

    (bar transpose 0 repeat 4) ~
    (bar transpose 5 repeat 2) ~
    (bar transpose 0 repeat 2) ~
    (bar transpose 7 repeat 1) ~
    (bar transpose 5 repeat 1) ~
    (bar transpose 0 repeat 2)
  }
}
