package compose.core

import cats.Order
import cats.instances.int._

import scalajs.js.annotation.{JSExport, JSExportTopLevel, JSExportAll}

@JSExportAll
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

@JSExportTopLevel("Duration")
object Duration {
  @JSExport val Whole        = Duration(64)
  @JSExport val Half         = Duration(32)
  @JSExport val Quarter      = Duration(16)
  @JSExport val Eighth       = Duration(8)
  @JSExport val Sixteenth    = Duration(4)
  @JSExport val ThirtySecond = Duration(2)
  @JSExport val SixtyFourth  = Duration(1)

  implicit val order: Order[Duration] =
    Order.by(_.value)
}
