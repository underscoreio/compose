package compose

import compose.examples._
import compose.player._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  ScPlayer.withPlayer(4) { player =>
    while (true) {
      for (item <- menu) println(item)
      val number = io.StdIn.readLine()
      menu.find(_.number == number) match {
        case Some(item) => player.play(item.score, Tempo(120))
        case _ => // Do nothing
      }
    }
  }
}
