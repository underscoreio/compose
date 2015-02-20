package compose

import compose.player.{Player, Tempo}
import compose.score.Note._
import compose.score._

object Main extends App {
  // Demo scores ---------------------
  def duellingBanjos =
    ((E3.e + F3.e + G3.q + E3.q + F3.q + D3.q + E3.q + C3.q + D3.h) transpose 12) + Rest.h +
      ((G2.q + C3.q + C3.q + D3.q + E3.q + C3.q + E3.q + D3.h) transpose 12)

  def chordProgression =
    (C3.q | E3.q | G3.q) +
      (C3.q | F3.q | A3.q) +
      (D3.q | F3.q | As3.q) +
      (Ds3.q | G3.q | As3.q) +
      (Ds3.q | Gs3.q | C4.q) +
      (F3.q | Gs3.q | Cs4.q) +
      (Fs3.q | As3.q | Cs4.q) +
      (Fs3.q | B3.q | Ds4.q) +
      (Gs3.q | B3.q | E4.q) +
      (A3.q | Cs4.q | E4.q) +
      (A3.q | D4.q | Fs4.q) +
      (B3.q | D4.q | G4.q) +
      (C4.w | E4.w | G4.w)

  def scale(base: Score) =
    List(0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19, 21, 23, 24).
      map(base.transpose).
      reduceLeft(_ + _)

  def echo(score: Score, duration: Duration): Score =
    score | (Rest(duration) + score)

  def powerChord(base: Score) =
    base | (base transpose 7)

  def smokeOnTheWater = {
    import compose.tab._
    tab"""
    # Smoke on the Water

    --------------------------------
    --------------------------------
    --------------------------------
    2-x-5-x-7-----2-x-5-x-8-7-------
    2-x-5-x-7-----2-x-5-x-8-7-------
    0-x-3-x-5-----0-x-3-x-6-5-------

    --------------------------------
    --------------------------------
    --------------------------------
    2-x-5-x-7-----5---2-------------
    2-x-5-x-7-----5---2-------------
    0-x-3-x-5-----3---0-------------
    """
  }

  def twelveBar = {
    val bar =
      (E3.q | B3.q) +
        (E3.s | B3.s) +
        (E3.q | Cs4.q) +
        (E3.s | B3.s)

    bar * 4 +
      (bar transpose 5) * 2 +
      bar * 2 +
      (bar transpose 7) +
      (bar transpose 5) +
      bar * 2
  }

  case class MenuItem(number: String, name: String, score: Score) {
    override def toString = s"$number. $name"
  }

  val menu = List(
    MenuItem("1", "Duelling Banjos", duellingBanjos),
    MenuItem("2", "Chord Progression", chordProgression),
    MenuItem("3", "Scale", scale(C3.s) * 4),
    MenuItem("4", "Scale With Echo", echo(scale(C3.s) * 4, Duration.Eigth.dotted)),
    MenuItem("5", "Smoke On The Water", smokeOnTheWater),
    MenuItem("6", "Twelve Bar Blues", twelveBar)
  )

  Player.withPlayer(4) { player =>
    implicit val tempo = Tempo(120)
    while (true) {
      for (item <- menu) println(item)
      val number = io.StdIn.readLine()
      menu.find(_.number == number) match {
        case Some(item) => player.play(item.score)
        case _ => // Do nothing
      }
    }
  }
}
