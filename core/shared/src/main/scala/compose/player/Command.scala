package compose.player

sealed trait Command
case class PitchOn(channel: Int, freq: Double) extends Command
case class PitchOff(channel: Int) extends Command
case class Wait(millis: Long) extends Command {
  def >(that: Wait) = this.millis > that.millis
  def <(that: Wait) = this.millis < that.millis
  def +(that: Wait) = Wait(this.millis + that.millis)
  def -(that: Wait) = Wait(this.millis - that.millis)
}
