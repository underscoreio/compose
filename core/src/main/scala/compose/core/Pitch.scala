package compose.core

import scalajs.js.annotation.{JSExport, JSExportTopLevel, JSExportAll}

@JSExportAll
case class Pitch(value: Int) extends PitchMethods

@JSExportTopLevel("Pitch")
object Pitch {

  @JSExport("create")
  def apply(offset: Int, octave: Int): Pitch =
    Pitch((12 * octave + offset) - (12 * 4 + 9))

  @JSExport val C0  = Pitch( 0, 0)
  @JSExport val Cs0 = Pitch( 1, 0)
  @JSExport val Db0 = Cs0
  @JSExport val D0  = Pitch( 2, 0)
  @JSExport val Ds0 = Pitch( 3, 0)
  @JSExport val Eb0 = Ds0
  @JSExport val E0  = Pitch( 4, 0)
  @JSExport val F0  = Pitch( 5, 0)
  @JSExport val Fs0 = Pitch( 6, 0)
  @JSExport val Gb0 = Fs0
  @JSExport val G0  = Pitch( 7, 0)
  @JSExport val Gs0 = Pitch( 8, 0)
  @JSExport val Ab0 = Gs0
  @JSExport val A0  = Pitch( 9, 0)
  @JSExport val As0 = Pitch(10, 0)
  @JSExport val Bb0 = As0
  @JSExport val B0  = Pitch(11, 0)

  @JSExport val C1  = Pitch( 0, 1)
  @JSExport val Cs1 = Pitch( 1, 1)
  @JSExport val Db1 = Cs1
  @JSExport val D1  = Pitch( 2, 1)
  @JSExport val Ds1 = Pitch( 3, 1)
  @JSExport val Eb1 = Ds1
  @JSExport val E1  = Pitch( 4, 1)
  @JSExport val F1  = Pitch( 5, 1)
  @JSExport val Fs1 = Pitch( 6, 1)
  @JSExport val Gb1 = Fs1
  @JSExport val G1  = Pitch( 7, 1)
  @JSExport val Gs1 = Pitch( 8, 1)
  @JSExport val Ab1 = Gs1
  @JSExport val A1  = Pitch( 9, 1)
  @JSExport val As1 = Pitch(10, 1)
  @JSExport val Bb1 = As1
  @JSExport val B1  = Pitch(11, 1)

  @JSExport val C2  = Pitch( 0, 2)
  @JSExport val Cs2 = Pitch( 1, 2)
  @JSExport val Db2 = Cs2
  @JSExport val D2  = Pitch( 2, 2)
  @JSExport val Ds2 = Pitch( 3, 2)
  @JSExport val Eb2 = Ds2
  @JSExport val E2  = Pitch( 4, 2)
  @JSExport val F2  = Pitch( 5, 2)
  @JSExport val Fs2 = Pitch( 6, 2)
  @JSExport val Gb2 = Fs2
  @JSExport val G2  = Pitch( 7, 2)
  @JSExport val Gs2 = Pitch( 8, 2)
  @JSExport val Ab2 = Gs2
  @JSExport val A2  = Pitch( 9, 2)
  @JSExport val As2 = Pitch(10, 2)
  @JSExport val Bb2 = As2
  @JSExport val B2  = Pitch(11, 2)

  @JSExport val C3  = Pitch( 0, 3)
  @JSExport val Cs3 = Pitch( 1, 3)
  @JSExport val Db3 = Cs3
  @JSExport val D3  = Pitch( 2, 3)
  @JSExport val Ds3 = Pitch( 3, 3)
  @JSExport val Eb3 = Ds3
  @JSExport val E3  = Pitch( 4, 3)
  @JSExport val F3  = Pitch( 5, 3)
  @JSExport val Fs3 = Pitch( 6, 3)
  @JSExport val Gb3 = Fs3
  @JSExport val G3  = Pitch( 7, 3)
  @JSExport val Gs3 = Pitch( 8, 3)
  @JSExport val Ab3 = Gs3
  @JSExport val A3  = Pitch( 9, 3)
  @JSExport val As3 = Pitch(10, 3)
  @JSExport val Bb3 = As3
  @JSExport val B3  = Pitch(11, 3)

  @JSExport val C4  = Pitch( 0, 4)
  @JSExport val Cs4 = Pitch( 1, 4)
  @JSExport val Db4 = Cs4
  @JSExport val D4  = Pitch( 2, 4)
  @JSExport val Ds4 = Pitch( 3, 4)
  @JSExport val Eb4 = Ds4
  @JSExport val E4  = Pitch( 4, 4)
  @JSExport val F4  = Pitch( 5, 4)
  @JSExport val Fs4 = Pitch( 6, 4)
  @JSExport val Gb4 = Fs4
  @JSExport val G4  = Pitch( 7, 4)
  @JSExport val Gs4 = Pitch( 8, 4)
  @JSExport val Ab4 = Gs4
  @JSExport val A4  = Pitch( 9, 4)
  @JSExport val As4 = Pitch(10, 4)
  @JSExport val Bb4 = As4
  @JSExport val B4  = Pitch(11, 4)

  @JSExport val C5  = Pitch( 0, 5)
  @JSExport val Cs5 = Pitch( 1, 5)
  @JSExport val Db5 = Cs5
  @JSExport val D5  = Pitch( 2, 5)
  @JSExport val Ds5 = Pitch( 3, 5)
  @JSExport val Eb5 = Ds5
  @JSExport val E5  = Pitch( 4, 5)
  @JSExport val F5  = Pitch( 5, 5)
  @JSExport val Fs5 = Pitch( 6, 5)
  @JSExport val Gb5 = Fs5
  @JSExport val G5  = Pitch( 7, 5)
  @JSExport val Gs5 = Pitch( 8, 5)
  @JSExport val Ab5 = Gs5
  @JSExport val A5  = Pitch( 9, 5)
  @JSExport val As5 = Pitch(10, 5)
  @JSExport val Bb5 = As5
  @JSExport val B5  = Pitch(11, 5)

  @JSExport val C6  = Pitch( 0, 6)
  @JSExport val Cs6 = Pitch( 1, 6)
  @JSExport val Db6 = Cs6
  @JSExport val D6  = Pitch( 2, 6)
  @JSExport val Ds6 = Pitch( 3, 6)
  @JSExport val Eb6 = Ds6
  @JSExport val E6  = Pitch( 4, 6)
  @JSExport val F6  = Pitch( 5, 6)
  @JSExport val Fs6 = Pitch( 6, 6)
  @JSExport val Gb6 = Fs6
  @JSExport val G6  = Pitch( 7, 6)
  @JSExport val Gs6 = Pitch( 8, 6)
  @JSExport val Ab6 = Gs6
  @JSExport val A6  = Pitch( 9, 6)
  @JSExport val As6 = Pitch(10, 6)
  @JSExport val Bb6 = As6
  @JSExport val B6  = Pitch(11, 6)

  import scala.language.implicitConversions

  implicit def pitchToScore(pitch: Pitch)(implicit duration: Duration) =
    Note(pitch, duration)
}

trait PitchMethods {
  self: Pitch =>

  def transpose(offset: Int): Pitch =
    Pitch(value + offset)

  def frequency: Double =
    math.pow(2, value / 12.0) * 440.0

  def w = Note(this, Duration.Whole)
  def h = Note(this, Duration.Half)
  def q = Note(this, Duration.Quarter)
  def e = Note(this, Duration.Eighth)
  def s = Note(this, Duration.Sixteenth)
  def t = Note(this, Duration.ThirtySecond)

  def <(that: Pitch)  = this.value <  that.value
  def >(that: Pitch)  = this.value >  that.value
  def <=(that: Pitch) = this.value <= that.value
  def >=(that: Pitch) = this.value >= that.value
}

