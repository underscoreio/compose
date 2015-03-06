package compose.core

case class Note(value: Int) {
  def transpose(transpose: Int): Note =
    Note(value + transpose)

  def apply(duration: Duration): NoteScore = NoteScore(this, duration)

  def w = NoteScore(this, Duration.Whole)
  def h = NoteScore(this, Duration.Half)
  def q = NoteScore(this, Duration.Quarter)
  def e = NoteScore(this, Duration.Eigth)
  def s = NoteScore(this, Duration.Sixteenth)
  def t = NoteScore(this, Duration.ThirtySecond)
}

object Note {
  import scala.language.implicitConversions

  def apply(offset: Int, octave: Int): Note =
    Note((12 * octave + offset) - (12 * 4 + 9))

  implicit def noteToScore(note: Note)(implicit duration: Duration) =
    NoteScore(note, duration)

  def withDuration[A](duration: Duration)(func: Duration => A): A =
    func(duration)

  val C0  = Note( 0, 0)
  val Cs0 = Note( 1, 0)
  val Db0 = Cs0
  val D0  = Note( 2, 0)
  val Ds0 = Note( 3, 0)
  val Eb0 = Ds0
  val E0  = Note( 4, 0)
  val F0  = Note( 5, 0)
  val Fs0 = Note( 6, 0)
  val Gb0 = Fs0
  val G0  = Note( 7, 0)
  val Gs0 = Note( 8, 0)
  val Ab0 = Gs0
  val A0  = Note( 9, 0)
  val As0 = Note(10, 0)
  val Bb0 = As0
  val B0  = Note(11, 0)

  val C1  = Note( 0, 1)
  val Cs1 = Note( 1, 1)
  val Db1 = Cs1
  val D1  = Note( 2, 1)
  val Ds1 = Note( 3, 1)
  val Eb1 = Ds1
  val E1  = Note( 4, 1)
  val F1  = Note( 5, 1)
  val Fs1 = Note( 6, 1)
  val Gb1 = Fs1
  val G1  = Note( 7, 1)
  val Gs1 = Note( 8, 1)
  val Ab1 = Gs1
  val A1  = Note( 9, 1)
  val As1 = Note(10, 1)
  val Bb1 = As1
  val B1  = Note(11, 1)

  val C2  = Note( 0, 2)
  val Cs2 = Note( 1, 2)
  val Db2 = Cs2
  val D2  = Note( 2, 2)
  val Ds2 = Note( 3, 2)
  val Eb2 = Ds2
  val E2  = Note( 4, 2)
  val F2  = Note( 5, 2)
  val Fs2 = Note( 6, 2)
  val Gb2 = Fs2
  val G2  = Note( 7, 2)
  val Gs2 = Note( 8, 2)
  val Ab2 = Gs2
  val A2  = Note( 9, 2)
  val As2 = Note(10, 2)
  val Bb2 = As2
  val B2  = Note(11, 2)

  val C3  = Note( 0, 3)
  val Cs3 = Note( 1, 3)
  val Db3 = Cs3
  val D3  = Note( 2, 3)
  val Ds3 = Note( 3, 3)
  val Eb3 = Ds3
  val E3  = Note( 4, 3)
  val F3  = Note( 5, 3)
  val Fs3 = Note( 6, 3)
  val Gb3 = Fs3
  val G3  = Note( 7, 3)
  val Gs3 = Note( 8, 3)
  val Ab3 = Gs3
  val A3  = Note( 9, 3)
  val As3 = Note(10, 3)
  val Bb3 = As3
  val B3  = Note(11, 3)

  val C4  = Note( 0, 4)
  val Cs4 = Note( 1, 4)
  val Db4 = Cs4
  val D4  = Note( 2, 4)
  val Ds4 = Note( 3, 4)
  val Eb4 = Ds4
  val E4  = Note( 4, 4)
  val F4  = Note( 5, 4)
  val Fs4 = Note( 6, 4)
  val Gb4 = Fs4
  val G4  = Note( 7, 4)
  val Gs4 = Note( 8, 4)
  val Ab4 = Gs4
  val A4  = Note( 9, 4)
  val As4 = Note(10, 4)
  val Bb4 = As4
  val B4  = Note(11, 4)

  val C5  = Note( 0, 5)
  val Cs5 = Note( 1, 5)
  val Db5 = Cs5
  val D5  = Note( 2, 5)
  val Ds5 = Note( 3, 5)
  val Eb5 = Ds5
  val E5  = Note( 4, 5)
  val F5  = Note( 5, 5)
  val Fs5 = Note( 6, 5)
  val Gb5 = Fs5
  val G5  = Note( 7, 5)
  val Gs5 = Note( 8, 5)
  val Ab5 = Gs5
  val A5  = Note( 9, 5)
  val As5 = Note(10, 5)
  val Bb5 = As5
  val B5  = Note(11, 5)

  val C6  = Note( 0, 6)
  val Cs6 = Note( 1, 6)
  val Db6 = Cs6
  val D6  = Note( 2, 6)
  val Ds6 = Note( 3, 6)
  val Eb6 = Ds6
  val E6  = Note( 4, 6)
  val F6  = Note( 5, 6)
  val Fs6 = Note( 6, 6)
  val Gb6 = Fs6
  val G6  = Note( 7, 6)
  val Gs6 = Note( 8, 6)
  val Ab6 = Gs6
  val A6  = Note( 9, 6)
  val As6 = Note(10, 6)
  val Bb6 = As6
  val B6  = Note(11, 6)
}
