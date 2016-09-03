package compose

import compose.core._
import compose.player._

import scalajs.js.JSApp
import scalajs.js.annotation.JSExport
import scalajs.concurrent.JSExecutionContext.Implicits.queue

@JSExport
object Main extends JSApp {

  @JSExport
  def play(score: Score, sampleUrl: String = "samples/bell.wav", tempo: Tempo = Tempo(120)): Unit = {
    val player = new compose.player.WebAudioPlayer(sampleUrl)
    player.play(score, tempo)
  }

  @JSExport
  def main(): Unit = {}
}
