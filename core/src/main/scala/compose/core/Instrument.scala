package compose.core

sealed abstract class Instrument extends Product with Serializable
case class Tuned(sample: Sample, tuning: Int = 0) extends Instrument
case class Fixed(sample: Sample, pitch: Pitch = Pitch.A4) extends Instrument
case class Combi(func: Pitch => Instrument) extends Instrument

object Instrument {
  val default: Instrument = Tuned(Sample.default)
}
