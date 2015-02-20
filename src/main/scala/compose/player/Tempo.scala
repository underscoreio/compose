package compose.player

import compose.score.Duration

case class Tempo(value: Int = 120) {
  def millis(duration: Duration): Long =
    ((duration.value / 64.0) * (120.0 / value) * 2000).toLong
}

