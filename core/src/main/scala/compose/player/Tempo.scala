package compose.player

import compose.core.Duration
import scalajs.js.annotation.JSExportAll

@JSExportAll
case class Tempo(bpm: Int = 120) {
  def milliseconds(dur: Duration): Long =
    ((dur.value / 64.0) * (120.0 / bpm) * 2000).toLong
}
