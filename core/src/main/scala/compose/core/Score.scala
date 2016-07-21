package compose.core

sealed trait Score {
  def ~(that: Score) = Score.Seq(this, that)
  def |(that: Score) = Score.Par(this, that)

  def repeat(num: Int): Score =
    (1 until num).foldLeft(this)((a, b) => a ~ this)

  def fold(pf: Pitch => Pitch = identity, df: Duration => Duration = identity): Score = this match {
    case Score.Empty      => Score.Empty
    case Score.Note(n, d) => Score.Note(pf(n), df(d))
    case Score.Rest(d)    => Score.Rest(df(d))
    case Score.Seq(a, b)  => Score.Seq(a.fold(pf, df), b.fold(pf, df))
    case Score.Par(a, b)  => Score.Par(a.fold(pf, df), b.fold(pf, df))
  }

  def transpose(t: Int): Score = this.fold(pf = _ transpose t)
  def halfTime: Score          = this.fold(df = _.halfTime)
  def doubleTime: Score        = this.fold(df = _.doubleTime)
  def dotted: Score            = this.fold(df = _.dotted)
  def doubleDotted: Score      = this.fold(df = _.doubleDotted)
  def tripleDotted: Score      = this.fold(df = _.tripleDotted)

  def simplify: Score = this match {
    case Score.Empty      => this
    case Score.Note(n, d) => this
    case Score.Rest(d)    => this
    case Score.Seq(a, b)  => Score.simplify(a, b)(Score.Seq.apply)
    case Score.Par(a, b)  => Score.simplify(a, b)(Score.Par.apply)
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

  def simplify(a: Score, b: Score)(func: (Score, Score) => Score): Score =
    (a.simplify, b.simplify) match {
      case (Score.Empty , Score.Empty) => Score.Empty
      case (Score.Empty , b          ) => b
      case (a           , Score.Empty) => a
      case (a           , b          ) => func(a, b)
    }
}
