package compose.tablature

import compose.core._
import scala.annotation.tailrec
import scala.reflect.macros.blackbox.Context
import scala.language.experimental.macros

class TablatureSyntax(val c: Context) extends StringHelpers {
  import c.universe._

  import Duration.{ Sixteenth => baseDuration }
  import Pitch.{ E3 => basePitch }

  def tabMacro(args: c.Tree *): c.Tree =
    if(args.length == 0) {
      c.prefix.tree match {
        case Apply(_, List(Apply(_, partTrees))) =>
          val tablature = partTrees map {
            case Literal(Constant(part: String)) => part
          } mkString ""

          val lines: List[String] = tablature.split("\n").flatMap(trimComments).toList
          val score: c.Tree       = render(parse(lines))
          q"$score.simplify"

        case _ => fail("Invalid tablature")
      }
    } else {
      fail("Unquoting is not supported")
    }

  def fail(msg: String) =
    c.abort(c.enclosingPosition, msg)

  def trimComments(str: String) = {
    val index = str indexOf '#'
    val line  = if(index < 0) str.trim else str.substring(0, index).trim
    if(line == "") None else Some(line)
  }

  def render(score: Score): c.Tree = score match {
    case EmptyScore                       => q"EmptyScore"
    case Note(Pitch(n), Duration(d)) => q"Note(Pitch($n), Duration($d))"
    case Rest(Duration(d))           => q"Rest(Duration($d))"
    case SeqScore(a, b)                   => q"SeqScore(${render(a)}, ${render(b)})"
    case ParScore(a, b)                   => q"ParScore(${render(a)}, ${render(b)})"
  }

  def parse(lines: List[String]): Score = {
    lines match {
      case highE :: b :: g :: d :: a :: lowE :: rest =>
        (
          parseString(highE, 24) |
          parseString(b,     19) |
          parseString(g,     15) |
          parseString(d,     10) |
          parseString(a,      5) |
          parseString(lowE,   0)
        ) ~ parse(rest)

      case Nil =>
        EmptyScore

      case _ =>
        fail("Incorrect number of lines: " + lines)
    }
  }

  def parseString(str: String, openPitch: Int): Score = {
    @tailrec def loop(str: String, accum: StringAccum): Score =
      str match {
        case ""                          => accum.completePitch.score
        case StartsWithFret(fret , rest) => loop(rest, accum.completePitch.startPitch(openPitch + fret))
        case StartsWithChar('x'  , rest) => loop(rest, accum.completePitch.startRest)
        case StartsWithChar('|'  , rest) => loop(rest, accum)
        case StartsWithChar(' '  , rest) => loop(rest, accum)
        case StartsWithChar(_    , rest) => loop(rest, accum.extendPitch)
      }

    loop(str, StringAccum(None, 0, EmptyScore))
  }

  case class StringAccum(pitch: Option[Pitch], length: Int, score: Score) {
    def startPitch(offset: Int): StringAccum =
      copy(pitch = Some(basePitch transpose offset), length = 1)

    def startRest: StringAccum =
      copy(pitch = None, length = 1)

    def extendPitch: StringAccum =
      copy(length = length + 1)

    def completePitch: StringAccum =
      if(length > 0) {
        pitch match {
          case Some(p) => copy(pitch = None, length = 0, score = score ~ Note(p, baseDuration * length))
          case _       => copy(pitch = None, length = 0, score = score ~ Rest(baseDuration * length))
        }
      } else copy(pitch = None, length = 0)
  }
}