package compose.player

import compose.core._
import scala.concurrent.{ExecutionContext => EC, _}
import scalajs.js.annotation.JSExport

trait Player[State] {
  import Command._

  @JSExport
  def play(score: Score, tempo: Tempo)(implicit ec: EC): Future[State] =
    initialise(score, tempo)
      .flatMap(playCommands(Compile(score)))
      .flatMap(shutdown)

  def initialise(score: Score, tempo: Tempo)(implicit ec: EC): Future[State]

  def shutdown(state: State)(implicit ec: EC): Future[State] =
    Future.successful(state)

  def playCommands(commands: Seq[Command])(state: State)(implicit ec: EC): Future[State] =
    commands match {
      case Nil =>
        Future.successful(state)

      case head +: tail =>
        playCommand(head)(state).flatMap(playCommands(tail))
    }

  def playCommand(cmd: Command)(state: State)(implicit ec: EC): Future[State]
}
