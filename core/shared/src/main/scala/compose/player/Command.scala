package compose.player

sealed trait Command
case class NoteOn(channel: Int, freq: Double) extends Command
case class NoteOff(channel: Int) extends Command
case class Wait(millis: Long) extends Command {
  def >(that: Wait) = this.millis > that.millis
  def <(that: Wait) = this.millis < that.millis
  def +(that: Wait) = Wait(this.millis + that.millis)
  def -(that: Wait) = Wait(this.millis - that.millis)
}
