package compose.examples

import compose.core._

trait Scale {
  import Score._
  import Pitch._

  def scale(base: Score) =
    List(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19, 21, 23, 24).
      map(base.transpose).
      reduceLeft(_ + _)

  def withDelay(score: Score, duration: Duration): Score =
    score | (Rest(duration) + score)
}
