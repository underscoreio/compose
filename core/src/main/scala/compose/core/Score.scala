package compose.core

sealed trait Score {
  def +(that: Score) = Score.Seq(this, that)
  def |(that: Score) = Score.Par(this, that)
  def repeat(num: Int) =
    (1 until num).foldLeft[Score](this)((a, b) => a + this)

  def halfTime: Score = this match {
    case Score.Empty      => Score.Empty
    case Score.Note(n, d) => Score.Note(n, d * 2)
    case Score.Rest(d)    => Score.Rest(d * 2)
    case Score.Seq(a, b)  => Score.Seq(a.halfTime, b.halfTime)
    case Score.Par(a, b)  => Score.Par(a.halfTime, b.halfTime)
  }

  def doubleTime: Score = this match {
    case Score.Empty      => Score.Empty
    case Score.Note(n, d) => Score.Note(n, d / 2)
    case Score.Rest(d)    => Score.Rest(d / 2)
    case Score.Seq(a, b)  => Score.Seq(a.doubleTime, b.doubleTime)
    case Score.Par(a, b)  => Score.Par(a.doubleTime, b.doubleTime)
  }

  def transpose(t: Int): Score = this match {
    case Score.Empty      => Score.Empty
    case Score.Note(n, d) => Score.Note(n transpose t, d)
    case Score.Rest(d)    => Score.Rest(d)
    case Score.Seq(a, b)  => Score.Seq(a transpose t, b transpose t)
    case Score.Par(a, b)  => Score.Par(a transpose t, b transpose t)
  }

  def dotted: Score = this match {
    case Score.Empty      => Score.Empty
    case Score.Note(n, d) => Score.Note(n, d.dotted)
    case Score.Rest(d)    => Score.Rest(d.dotted)
    case Score.Seq(a, b)  => Score.Seq(a.dotted, b.dotted)
    case Score.Par(a, b)  => Score.Par(a.dotted, b.dotted)
  }

  def doubleDotted: Score = this match {
    case Score.Empty      => Score.Empty
    case Score.Note(n, d) => Score.Note(n, d.doubleDotted)
    case Score.Rest(d)    => Score.Rest(d.doubleDotted)
    case Score.Seq(a, b)  => Score.Seq(a.doubleDotted, b.doubleDotted)
    case Score.Par(a, b)  => Score.Par(a.doubleDotted, b.doubleDotted)
  }

  def tripleDotted: Score = this match {
    case Score.Empty      => Score.Empty
    case Score.Note(n, d) => Score.Note(n, d.tripleDotted)
    case Score.Rest(d)    => Score.Rest(d.tripleDotted)
    case Score.Seq(a, b)  => Score.Seq(a.tripleDotted, b.tripleDotted)
    case Score.Par(a, b)  => Score.Par(a.tripleDotted, b.tripleDotted)
  }

  def normalize: Score = this match {
    case Score.Empty      => this
    case Score.Note(n, d) => this
    case Score.Rest(d)    => this
    case Score.Seq(a, b)  =>
      (a.normalize, b.normalize) match {
        case (Score.Empty , Score.Empty) => Score.Empty
        case (Score.Empty , b         ) => b
        case (a          , Score.Empty) => a
        case (a          , b         ) => Score.Seq(a, b)
      }
    case Score.Par(a, b)  =>
      (a.normalize, b.normalize) match {
        case (Score.Empty , Score.Empty) => Score.Empty
        case (Score.Empty , b         ) => b
        case (a          , Score.Empty) => a
        case (a          , b         ) => Score.Par(a, b)
      }
  }
}

object Score {
  case object Empty extends Score
  case class Note(pitch: Pitch, duration: Duration) extends Score
  case class Rest(duration: Duration) extends Score
  case class Seq(a: Score, b: Score) extends Score
  case class Par(a: Score, b: Score) extends Score

  object Rest {
    def w = Rest(Duration.Whole)
    def h = Rest(Duration.Half)
    def q = Rest(Duration.Quarter)
    def e = Rest(Duration.Eighth)
    def s = Rest(Duration.Sixteenth)
    def t = Rest(Duration.ThirtySecond)
  }
}
