package compose

import compose.core._
import compose.player._
import compose.examples.all._

import scalajs.js.JSApp
import scalajs.js.annotation.JSExportAll
import scalajs.concurrent.JSExecutionContext.Implicits.queue

@JSExportAll
object Main extends JSApp {
  // Player

  val player = new WebAudioPlayer(sample => s"samples/${sample.name}.wav")

  // Samples

  val Bell  = Sample("bell")
  val Beep1 = Sample("beep1")
  val Beep2 = Sample("beep2")
  val Kick  = Sample("kick")
  val Snare = Sample("snare")
  val Hat   = Sample("hat")
  val Meow  = Sample("meow")

  // Instruments

  val bell = Tuned(Bell)
  val beep = Tuned(Beep1, -12)
  val meow = Tuned(Meow, -12)

  val kit = Combi {
    case Pitch.C4 => Fixed(Kick)
    case Pitch.D4 => Fixed(Snare)
    case Pitch.E4 => Fixed(Hat)
    case _        => Tuned(Meow)
  }

  // Score

  val drums = {
    import Pitch._
    (E4.q repeat 32) | (C4.h ~ D4.h repeat 8)
  }

  val score =
    (Rest.q ~ Arrange(drums, kit)) |
    Arrange(duellingBanjos, beep) |
    (Rest.q ~ Arrange(smokeOnTheWater, bell))

  // PLAY ALL THE THINGS!

  def main(): Unit =
    player.play(score, Tempo(180))
}
