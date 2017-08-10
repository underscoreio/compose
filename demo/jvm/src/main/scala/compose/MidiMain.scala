package compose

import compose.core._
import compose.examples._
import compose.player._
import scala.concurrent.Await
import scala.concurrent.duration.{Duration => Dur}
import scala.concurrent.ExecutionContext.Implicits.global

object MidiMain extends App {
  case class MenuItem(number: String, name: String, score: Score) {
    override def toString = s"$number. $name"
  }

  val menu = List(
    MenuItem("1", "Duelling Banjos",    duellingBanjos),
    MenuItem("2", "Chord Progression",  chordProgression),
    MenuItem("3", "Scale",              scale(Pitch.C3.s) repeat 4),
    MenuItem("4", "Scale With Echo",    withDelay(scale(Pitch.C3.s) repeat 4, Duration.Eighth.dotted)),
    MenuItem("5", "Smoke On The Water", smokeOnTheWater),
    MenuItem("6", "Twelve Bar Blues",   twelveBarBlues),
    MenuItem("7", "Freebird!",          freebird),
    MenuItem("8", "Scale With Overlap", withDelay(scale(Pitch.C3.h) repeat 2, Duration.Quarter.doubleDotted))
  )

  JavaSoundPlayer.withPlayer() { player =>
    while (true) {
      for (item <- menu) println(item)
      val number = io.StdIn.readLine()
      menu
        .find(_.number == number)
        .foreach(item => Await.result(player.play(item.score, Tempo(180)), Dur.Inf))
    }
  }
}
