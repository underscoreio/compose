package compose.score

object Rest {
  def apply(duration: Duration): RestScore =
    RestScore(duration)

  def w = RestScore(Duration.Whole)
  def h = RestScore(Duration.Half)
  def q = RestScore(Duration.Quarter)
  def e = RestScore(Duration.Eigth)
  def s = RestScore(Duration.Sixteenth)
  def t = RestScore(Duration.ThirtySecond)
}