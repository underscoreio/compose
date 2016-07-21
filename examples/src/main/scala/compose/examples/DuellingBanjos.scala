package compose.examples

import compose.core._

trait DuellingBanjos {
  import Score._
  import Pitch._

  val duellingBanjos =
    ((E3.e ~ F3.e ~ G3.q ~ E3.q ~ F3.q ~ D3.q ~ E3.q ~ C3.q ~ D3.h) transpose 12) ~ Rest.h ~
    ((G2.q ~ C3.q ~ C3.q ~ D3.q ~ E3.q ~ C3.q ~ E3.q ~ D3.h       ) transpose 12)
}
