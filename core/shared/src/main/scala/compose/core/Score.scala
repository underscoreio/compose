package compose.core

sealed trait Score {
  def +(that: Score) = SeqScore(this, that)
  def |(that: Score) = ParScore(this, that)
  def repeat(num: Int) =
    (1 until num).foldLeft[Score](this)((a, b) => a + this)

  def halfTime: Score = this match {
    case EmptyScore      => EmptyScore
    case NoteScore(n, d) => NoteScore(n, d * 2)
    case RestScore(d)    => RestScore(d * 2)
    case SeqScore(a, b)  => SeqScore(a.halfTime, b.halfTime)
    case ParScore(a, b)  => ParScore(a.halfTime, b.halfTime)
  }

  def doubleTime: Score = this match {
    case EmptyScore      => EmptyScore
    case NoteScore(n, d) => NoteScore(n, d / 2)
    case RestScore(d)    => RestScore(d / 2)
    case SeqScore(a, b)  => SeqScore(a.doubleTime, b.doubleTime)
    case ParScore(a, b)  => ParScore(a.doubleTime, b.doubleTime)
  }

  def transpose(t: Int): Score = this match {
    case EmptyScore      => EmptyScore
    case NoteScore(n, d) => NoteScore(n transpose t, d)
    case RestScore(d)    => RestScore(d)
    case SeqScore(a, b)  => SeqScore(a transpose t, b transpose t)
    case ParScore(a, b)  => ParScore(a transpose t, b transpose t)
  }

  def dotted: Score = this match {
    case EmptyScore      => EmptyScore
    case NoteScore(n, d) => NoteScore(n, d.dotted)
    case RestScore(d)    => RestScore(d.dotted)
    case SeqScore(a, b)  => SeqScore(a.dotted, b.dotted)
    case ParScore(a, b)  => ParScore(a.dotted, b.dotted)
  }

  def doubleDotted: Score = this match {
    case EmptyScore      => EmptyScore
    case NoteScore(n, d) => NoteScore(n, d.doubleDotted)
    case RestScore(d)    => RestScore(d.doubleDotted)
    case SeqScore(a, b)  => SeqScore(a.doubleDotted, b.doubleDotted)
    case ParScore(a, b)  => ParScore(a.doubleDotted, b.doubleDotted)
  }

  def tripleDotted: Score = this match {
    case EmptyScore      => EmptyScore
    case NoteScore(n, d) => NoteScore(n, d.tripleDotted)
    case RestScore(d)    => RestScore(d.tripleDotted)
    case SeqScore(a, b)  => SeqScore(a.tripleDotted, b.tripleDotted)
    case ParScore(a, b)  => ParScore(a.tripleDotted, b.tripleDotted)
  }

  def normalize: Score = this match {
    case EmptyScore      => this
    case NoteScore(n, d) => this
    case RestScore(d)    => this
    case SeqScore(a, b)  =>
      (a.normalize, b.normalize) match {
        case (EmptyScore , EmptyScore) => EmptyScore
        case (EmptyScore , b         ) => b
        case (a          , EmptyScore) => a
        case (a          , b         ) => SeqScore(a, b)
      }
    case ParScore(a, b)  =>
      (a.normalize, b.normalize) match {
        case (EmptyScore , EmptyScore) => EmptyScore
        case (EmptyScore , b         ) => b
        case (a          , EmptyScore) => a
        case (a          , b         ) => ParScore(a, b)
      }
  }
}

case object EmptyScore extends Score
case class NoteScore(note: Note, duration: Duration) extends Score
case class RestScore(duration: Duration) extends Score
case class SeqScore(a: Score, b: Score) extends Score
case class ParScore(a: Score, b: Score) extends Score
