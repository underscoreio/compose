package compose

import compose.examples._
import compose.player._
import compose.player.Implicits._

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport object Main extends JSApp {

  @JSExport def main(): Unit = {
    import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
    compose.player.Player.play(smokeOnTheWater)
  }
}
