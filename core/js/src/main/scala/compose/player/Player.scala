package compose.player

import compose.core._

import scala.concurrent._
import scala.scalajs.js
import org.scalajs.dom

object Player {
  import Implicits._

  type EC = ExecutionContext

  def play(score: Score)(implicit ec: EC): Future[Unit] = {
    val ctx = js.Dynamic.newInstance(js.Dynamic.global.AudioContext)()
    implicit val tempo = 120.bpm

    loadSound(ctx, "web-audio-spike/cat9.wav").flatMap(playCommands(ctx, _, score.compile))
  }

  def loadSound(ctx: js.Dynamic, url: String): Future[js.Dynamic] = {
    var request = new dom.XMLHttpRequest()
    var promise = Promise[js.Dynamic]

    request.open("GET", url, true)
    request.responseType = "arraybuffer"

    request.onload = (evt: dom.Event) =>
      ctx.decodeAudioData(request.response, (buffer: js.Dynamic) => promise success buffer)

    request.send()

    promise.future
  }

  def playCommands(ctx: js.Dynamic, buffer: js.Dynamic, commands: Seq[Command])(implicit ec: EC): Future[Unit] = {
    commands match {
      case Nil =>
        Future.successful(())

      case (head: NoteOn) +: tail =>
        playNoteOn(ctx, buffer, head).flatMap(_ => playCommands(ctx, buffer, tail))

      case (head: Wait) +: tail =>
        playWait(ctx, head).flatMap(_ => playCommands(ctx, buffer, tail))

      case (head: NoteOff) +: tail =>
        playCommands(ctx, buffer, tail)
    }
  }

  def playNoteOn(ctx: js.Dynamic, buffer: js.Dynamic, cmd: NoteOn)(implicit ec: EC): Future[Unit] = {
    Future {
      var source = ctx.createBufferSource()
      source.buffer = buffer
      source.connect(ctx.destination)
      source.playbackRate.setValueAtTime(cmd.freq / 220.0, 0)
      source.start(0)
    }
  }

  def playWait(ctx: js.Dynamic, cmd: Wait)(implicit ec: EC): Future[Unit] = {
    val promise = Promise[Unit]
    dom.setTimeout((() => promise success ()), cmd.millis.toInt)
    promise.future
  }
}
