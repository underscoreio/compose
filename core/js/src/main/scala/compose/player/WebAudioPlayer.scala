package compose.player

import compose.core._
import org.scalajs.dom.{XMLHttpRequest, Event}
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.{AudioContext, AudioBuffer}
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.concurrent.{ExecutionContext => EC, _}
import scala.scalajs.js

class WebAudioPlayer(
  sampleUrl: String = "web-audio-spike/cat9.wav",
  ctx: AudioContext = new AudioContext()
) extends Player {
  import Command._

  case class State(ctx: AudioContext, buffer: AudioBuffer)

  def initialise: Future[State] = {
    var request = new XMLHttpRequest()
    request.open("GET", sampleUrl, true)
    request.responseType = "arraybuffer"

    var promise = Promise[State]
    request.onload = (evt: Event) =>
      ctx.decodeAudioData(
        request.response.asInstanceOf[ArrayBuffer],
        (buffer: AudioBuffer) => promise.success(State(ctx, buffer))
      )

    request.send()
    promise.future
  }

  def playCommand(state: State, cmd: Command)(implicit ec: EC, tempo: Tempo): Future[State] = {
    cmd match {
      case cmd: NoteOn =>
        Future {
          var source = state.ctx.createBufferSource()
          source.buffer = state.buffer
          source.connect(state.ctx.destination)
          source.playbackRate.setValueAtTime(cmd.pitch.frequency / 220.0, 0)
          source.start(0)
          state
        }

      case cmd: NoteOff =>
        Future.successful(state)

      case cmd: Wait =>
        val promise = Promise[State]
        js.timers.setTimeout(tempo.milliseconds(cmd.duration)) {
          promise.success(state)
        }
        promise.future
    }
  }
}
