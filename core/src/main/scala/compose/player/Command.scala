package compose.player

import algebra.Order
import compose.core._

sealed trait Command

object Command {
  case class NoteOn(id: Int, pitch: Pitch) extends Command
  case class NoteOff(id: Int) extends Command
  case class Wait(duration: Duration) extends Command

  implicit val waitOrder: Order[Wait] = Order.by(_.duration)
}
