package compose.examples

import compose.core._
import scalajs.js.annotation.JSExport

trait DuellingBanjos {
  import Score._
  import Pitch._

  @JSExport
  val duellingBanjos =
    (E5.e ~ F5.e ~ G5.q ~ E5.q ~ F5.q ~ D5.q ~ E5.q ~ C5.q ~ D5.h) ~ Rest.h ~
    (G4.q ~ C5.q ~ C5.q ~ D5.q ~ E5.q ~ C5.q ~ E5.q ~ D5.h       )
}
