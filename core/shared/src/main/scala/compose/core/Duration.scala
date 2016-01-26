package compose.core

case class Duration(value: Int) {
  def dotted       = Duration(value *  3 / 2)
  def doubleDotted = Duration(value *  7 / 4)
  def tripleDotted = Duration(value * 15 / 8)
  def *(n: Int)    = Duration(value * n)
  def /(n: Int)    = Duration(value / n)
}

object Duration {
  val Whole        = Duration(64)
  val Half         = Duration(32)
  val Quarter      = Duration(16)
  val Eigth        = Duration(8)
  val Sixteenth    = Duration(4)
  val ThirtySecond = Duration(2)
  val SixtyFourth  = Duration(1)
}
