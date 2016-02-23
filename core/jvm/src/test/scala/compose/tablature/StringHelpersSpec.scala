package compose.tablature

import compose.core._
import org.scalatest._

class StringHelpersSpec extends FlatSpec with Matchers with StringHelpers {
  import Pitch._

  "StartsWithChar" should "handle an empty string" in {
    ("" match {
      case StartsWithChar(char, rest) => Some((char, rest))
      case _                          => None
    }) should equal(None)
  }

  it should "handle a string of length 1" in {
    ("a" match {
      case StartsWithChar(char, rest) => Some((char, rest))
      case _                          => None
    }) should equal(Some(('a', "")))
  }

  it should "handle a string of length >1" in {
    ("abc" match {
      case StartsWithChar(char, rest) => Some((char, rest))
      case _                          => None
    }) should equal(Some(('a', "bc")))
  }

  "StartsWithFret" should "handle an empty string" in {
    ("" match {
      case StartsWithFret(num, rest) => Some((num, rest))
      case _                           => None
    }) should equal(None)
  }

  it should "handle a string starting with a non-numeric character" in {
    ("abc" match {
      case StartsWithFret(num, rest) => Some((num, rest))
      case _                           => None
    }) should equal(None)
  }

  it should "handle a string starting with a single-digit number" in {
    ("1bc" match {
      case StartsWithFret(num, rest) => Some((num, rest))
      case _                           => None
    }) should equal(Some((1, "bc")))
  }

  it should "handle a string starting with a two-digit number" in {
    ("12c" match {
      case StartsWithFret(num, rest) => Some((num, rest))
      case _                           => None
    }) should equal(Some((12, "c")))
  }

  it should "handle a string starting and ending with a number" in {
    ("12" match {
      case StartsWithFret(num, rest) => Some((num, rest))
      case _                           => None
    }) should equal(Some((12, "")))
  }
}