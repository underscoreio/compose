package compose.core

import algebra.Order
import algebra.std.int._

case class Duration(value: Int) {
  def halfTime          = Duration(value *  2)
  def doubleTime        = Duration(value /  2)
  def dotted            = Duration(value *  3 / 2)
  def doubleDotted      = Duration(value *  7 / 4)
  def tripleDotted      = Duration(value * 15 / 8)
  def +(that: Duration) = Duration(this.value + that.value)
  def -(that: Duration) = Duration(this.value - that.value)
  def *(n: Int)         = Duration(value * n)
  def /(n: Int)         = Duration(value / n)
}

object Duration {
  val Whole        = Duration(64)
  val Half         = Duration(32)
  val Quarter      = Duration(16)
  val Eighth       = Duration(8)
  val Sixteenth    = Duration(4)
  val ThirtySecond = Duration(2)
  val SixtyFourth  = Duration(1)

  implicit val order: Order[Duration] =
    Order.by(_.value)
}
