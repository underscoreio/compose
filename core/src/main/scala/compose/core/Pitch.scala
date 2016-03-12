package compose.core

case class Pitch(value: Int) {
  def transpose(transpose: Int): Pitch =
    Pitch(value + transpose)

  def frequency: Double =
    math.pow(2, value / 12.0) * 440.0

  def w = Score.Note(this, Duration.Whole)
  def h = Score.Note(this, Duration.Half)
  def q = Score.Note(this, Duration.Quarter)
  def e = Score.Note(this, Duration.Eighth)
  def s = Score.Note(this, Duration.Sixteenth)
  def t = Score.Note(this, Duration.ThirtySecond)
}

object Pitch {
  import scala.language.implicitConversions

  def apply(offset: Int, octave: Int): Pitch =
    Pitch((12 * octave + offset) - (12 * 4 + 9))

  implicit def pitchToScore(pitch: Pitch)(implicit duration: Duration) =
    Score.Note(pitch, duration)

  def withDuration[A](duration: Duration)(func: Duration => A): A =
    func(duration)

  val C0  = Pitch( 0, 0)
  val Cs0 = Pitch( 1, 0)
  val Db0 = Cs0
  val D0  = Pitch( 2, 0)
  val Ds0 = Pitch( 3, 0)
  val Eb0 = Ds0
  val E0  = Pitch( 4, 0)
  val F0  = Pitch( 5, 0)
  val Fs0 = Pitch( 6, 0)
  val Gb0 = Fs0
  val G0  = Pitch( 7, 0)
  val Gs0 = Pitch( 8, 0)
  val Ab0 = Gs0
  val A0  = Pitch( 9, 0)
  val As0 = Pitch(10, 0)
  val Bb0 = As0
  val B0  = Pitch(11, 0)

  val C1  = Pitch( 0, 1)
  val Cs1 = Pitch( 1, 1)
  val Db1 = Cs1
  val D1  = Pitch( 2, 1)
  val Ds1 = Pitch( 3, 1)
  val Eb1 = Ds1
  val E1  = Pitch( 4, 1)
  val F1  = Pitch( 5, 1)
  val Fs1 = Pitch( 6, 1)
  val Gb1 = Fs1
  val G1  = Pitch( 7, 1)
  val Gs1 = Pitch( 8, 1)
  val Ab1 = Gs1
  val A1  = Pitch( 9, 1)
  val As1 = Pitch(10, 1)
  val Bb1 = As1
  val B1  = Pitch(11, 1)

  val C2  = Pitch( 0, 2)
  val Cs2 = Pitch( 1, 2)
  val Db2 = Cs2
  val D2  = Pitch( 2, 2)
  val Ds2 = Pitch( 3, 2)
  val Eb2 = Ds2
  val E2  = Pitch( 4, 2)
  val F2  = Pitch( 5, 2)
  val Fs2 = Pitch( 6, 2)
  val Gb2 = Fs2
  val G2  = Pitch( 7, 2)
  val Gs2 = Pitch( 8, 2)
  val Ab2 = Gs2
  val A2  = Pitch( 9, 2)
  val As2 = Pitch(10, 2)
  val Bb2 = As2
  val B2  = Pitch(11, 2)

  val C3  = Pitch( 0, 3)
  val Cs3 = Pitch( 1, 3)
  val Db3 = Cs3
  val D3  = Pitch( 2, 3)
  val Ds3 = Pitch( 3, 3)
  val Eb3 = Ds3
  val E3  = Pitch( 4, 3)
  val F3  = Pitch( 5, 3)
  val Fs3 = Pitch( 6, 3)
  val Gb3 = Fs3
  val G3  = Pitch( 7, 3)
  val Gs3 = Pitch( 8, 3)
  val Ab3 = Gs3
  val A3  = Pitch( 9, 3)
  val As3 = Pitch(10, 3)
  val Bb3 = As3
  val B3  = Pitch(11, 3)

  val C4  = Pitch( 0, 4)
  val Cs4 = Pitch( 1, 4)
  val Db4 = Cs4
  val D4  = Pitch( 2, 4)
  val Ds4 = Pitch( 3, 4)
  val Eb4 = Ds4
  val E4  = Pitch( 4, 4)
  val F4  = Pitch( 5, 4)
  val Fs4 = Pitch( 6, 4)
  val Gb4 = Fs4
  val G4  = Pitch( 7, 4)
  val Gs4 = Pitch( 8, 4)
  val Ab4 = Gs4
  val A4  = Pitch( 9, 4)
  val As4 = Pitch(10, 4)
  val Bb4 = As4
  val B4  = Pitch(11, 4)

  val C5  = Pitch( 0, 5)
  val Cs5 = Pitch( 1, 5)
  val Db5 = Cs5
  val D5  = Pitch( 2, 5)
  val Ds5 = Pitch( 3, 5)
  val Eb5 = Ds5
  val E5  = Pitch( 4, 5)
  val F5  = Pitch( 5, 5)
  val Fs5 = Pitch( 6, 5)
  val Gb5 = Fs5
  val G5  = Pitch( 7, 5)
  val Gs5 = Pitch( 8, 5)
  val Ab5 = Gs5
  val A5  = Pitch( 9, 5)
  val As5 = Pitch(10, 5)
  val Bb5 = As5
  val B5  = Pitch(11, 5)

  val C6  = Pitch( 0, 6)
  val Cs6 = Pitch( 1, 6)
  val Db6 = Cs6
  val D6  = Pitch( 2, 6)
  val Ds6 = Pitch( 3, 6)
  val Eb6 = Ds6
  val E6  = Pitch( 4, 6)
  val F6  = Pitch( 5, 6)
  val Fs6 = Pitch( 6, 6)
  val Gb6 = Fs6
  val G6  = Pitch( 7, 6)
  val Gs6 = Pitch( 8, 6)
  val Ab6 = Gs6
  val A6  = Pitch( 9, 6)
  val As6 = Pitch(10, 6)
  val Bb6 = As6
  val B6  = Pitch(11, 6)
}
