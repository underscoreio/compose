package compose.player

import compose.core._
import scala.concurrent.{ExecutionContext => EC, _}
import scalajs.js.annotation.JSExport

trait Player[State] {
  import Command._

  @JSExport
  def play(score: Score, tempo: Tempo)(implicit ec: EC): Future[State] = {
    val commands = Compile(score)
    for {
      state <- initialise(score)
      state <- playCommands(state, commands)(ec, tempo)
      state <- shutdown(state)
    } yield state
  }

  def initialise(score: Score): Future[State]

  def shutdown(state: State): Future[State] =
    Future.successful(state)

  def playCommands(state: State, commands: Seq[Command])(implicit ec: EC, tempo: Tempo): Future[State] =
    commands match {
      case Nil =>
        Future.successful(state)

      case head +: tail =>
        playCommand(state, head) flatMap (state => playCommands(state, tail))
    }

  def playCommand(state: State, cmd: Command)(implicit ec: EC, tempo: Tempo): Future[State]
}
