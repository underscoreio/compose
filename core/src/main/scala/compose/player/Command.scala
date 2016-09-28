package compose.player

import cats.Order
import compose.core._

sealed trait Command
case class NoteOn(id: Int, sample: Sample, pitch: Pitch) extends Command
case class NoteOff(id: Int) extends Command
case class Wait(duration: Duration) extends Command

object Command {
  implicit val waitOrder: Order[Wait] = Order.by(_.duration)
}
