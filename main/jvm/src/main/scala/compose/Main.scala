package compose

import compose.examples._
import compose.player._
import compose.player.Implicits._

object Main extends App {
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
