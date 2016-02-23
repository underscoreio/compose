package compose

import compose.core._
import compose.core.Score.{ Rest => r }
import compose.core.Pitch._

object examples {
  def duellingBanjos =
    ((E3.e + F3.e + G3.q + E3.q + F3.q + D3.q + E3.q + C3.q + D3.h) transpose 12) + r.h +
    ((G2.q + C3.q + C3.q + D3.q + E3.q + C3.q + E3.q + D3.h       ) transpose 12)

  def chordProgression =
    ( C3.q  | E3.q  | G3.q  ) +
    ( C3.q  | F3.q  | A3.q  ) +
    ( D3.q  | F3.q  | As3.q ) +
    ( Ds3.q | G3.q  | As3.q ) +
    ( Ds3.q | Gs3.q | C4.q  ) +
    ( F3.q  | Gs3.q | Cs4.q ) +
    ( Fs3.q | As3.q | Cs4.q ) +
    ( Fs3.q | B3.q  | Ds4.q ) +
    ( Gs3.q | B3.q  | E4.q  ) +
    ( A3.q  | Cs4.q | E4.q  ) +
    ( A3.q  | D4.q  | Fs4.q ) +
    ( B3.q  | D4.q  | G4.q  ) +
    ( C4.w  | E4.w  | G4.w  )

  def scale(base: Score) =
    List(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19, 21, 23, 24).
      map(base.transpose).
      reduceLeft(_ + _)

  def echo(score: Score, duration: Duration): Score =
    score | (r(duration) + score)

  def powerChord(base: Score) =
    base | (base transpose 7)

  def smokeOnTheWater = (
    (E4.e + r.e + G4.e + r.e + A4.q.dotted + E4.e + r.e + G4.e + r.e + As4.e + A4.h) |
    (B3.e + r.e + D4.e + r.e + E4.q.dotted + B3.e + r.e + D4.e + r.e + F4.e  + E4.h) |
    (E3.e + r.e + G3.e + r.e + A3.q.dotted + E3.e + r.e + G3.e + r.e + As3.e + A3.h)
  ) + (
    (E4.e + r.e + G4.e + r.e + A4.q.dotted + G4.e + r.e + E4.h.doubleDotted) |
    (B3.e + r.e + D4.e + r.e + E4.q.dotted + D4.e + r.e + B3.h.doubleDotted) |
    (E3.e + r.e + G3.e + r.e + A3.q.dotted + G3.e + r.e + E3.h.doubleDotted)
  )

  def twelveBar = {
    val bar =
      ( E3.q | B3.q  ) +
      ( E3.s | B3.s  ) +
      ( E3.q | Cs4.q ) +
      ( E3.s | B3.s  )

    (bar transpose 0 repeat 4) +
    (bar transpose 5 repeat 2) +
    (bar transpose 0 repeat 2) +
    (bar transpose 7 repeat 1) +
    (bar transpose 5 repeat 1) +
    (bar transpose 0 repeat 2)
  }

  val freebird = {
    import compose.tablature._

    tab"""
    |-------------------------------|----------------------------|
    |-3------------3---3---3-3------|-5--x-----------------------|
    |-4------------3-x-3-x-3-3--x-x-|-5--x-----------------------|
    |-5--x-------x-3-x-3-x-3-3--x-x-|-5--x-----5----5----5----5--|
    |-5--x---0-2-x-1-x-1-x-1-1--x-x-|-3----5h7----7----7----7----|
    |-3--x-3-----x------------------|----------------------------|
    """
  }

  case class MenuItem(number: String, name: String, score: Score) {
    override def toString = s"$number. $name"
  }

  val menu = List(
    MenuItem("1", "Duelling Banjos",    duellingBanjos),
    MenuItem("2", "Chord Progression",  chordProgression),
    MenuItem("3", "Scale",              scale(C3.s) repeat 4),
    MenuItem("4", "Scale With Echo",    echo(scale(C3.s) repeat 4, Duration.Eigth.dotted)),
    MenuItem("5", "Smoke On The Water", smokeOnTheWater),
    MenuItem("6", "Twelve Bar Blues",   twelveBar),
    MenuItem("7", "Freebird!",          freebird)
  )
}