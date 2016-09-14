package compose.core

import scalajs.js.annotation.{JSExport, JSExportAll}

@JSExportAll
sealed abstract class Score extends ScoreMethods with Product with Serializable

@JSExportAll case object EmptyScore extends Score
@JSExportAll case class Note(pitch: Pitch, duration: Duration) extends Score
@JSExportAll case class Rest(duration: Duration) extends Score
@JSExportAll case class SeqScore(a: Score, b: Score) extends Score
@JSExportAll case class ParScore(a: Score, b: Score) extends Score

@JSExportAll object Rest {
  val w = Rest(Duration.Whole)
  val h = Rest(Duration.Half)
  val q = Rest(Duration.Quarter)
  val e = Rest(Duration.Eighth)
  val s = Rest(Duration.Sixteenth)
  val t = Rest(Duration.ThirtySecond)
}

object Score {
  def simplify(a: Score, b: Score)(func: (Score, Score) => Score): Score =
    (a.simplify, b.simplify) match {
      case (EmptyScore , EmptyScore) => EmptyScore
      case (EmptyScore , b         ) => b
      case (a          , EmptyScore) => a
      case (a          , b         ) => func(a, b)
    }
}

trait ScoreMethods {
  self: Score =>

  @JSExport("then") def ~(that: Score) = SeqScore(this, that)
  @JSExport("and")  def |(that: Score) = ParScore(this, that)

  @JSExport def repeat(num: Int): Score =
    (1 until num).foldLeft(this)((a, b) => a ~ this)

  def fold(pf: Pitch => Pitch = identity, df: Duration => Duration = identity): Score =
    this match {
      case EmptyScore     => EmptyScore
      case Note(n, d)     => Note(pf(n), df(d))
      case Rest(d)        => Rest(df(d))
      case SeqScore(a, b) => SeqScore(a.fold(pf, df), b.fold(pf, df))
      case ParScore(a, b) => ParScore(a.fold(pf, df), b.fold(pf, df))
    }

  @JSExport def transpose(t: Int): Score = this.fold(pf = _ transpose t)

  @JSExport def halfTime: Score          = this.fold(df = _.halfTime)
  @JSExport def doubleTime: Score        = this.fold(df = _.doubleTime)
  @JSExport def dotted: Score            = this.fold(df = _.dotted)
  @JSExport def doubleDotted: Score      = this.fold(df = _.doubleDotted)
  @JSExport def tripleDotted: Score      = this.fold(df = _.tripleDotted)
  @JSExport def *(n: Int): Score         = this.fold(df = _ * n)
  @JSExport def /(n: Int): Score         = this.fold(df = _ / n)

  def simplify: Score = this match {
    case EmptyScore      => this
    case Note(n, d)      => this
    case Rest(d)         => this
    case SeqScore(a, b)  => Score.simplify(a, b)(SeqScore.apply)
    case ParScore(a, b)  => Score.simplify(a, b)(ParScore.apply)
  }
}