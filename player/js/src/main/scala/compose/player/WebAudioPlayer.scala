package compose.player

import compose.core._
import org.scalajs.dom.{XMLHttpRequest, Event}
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.raw.{AudioContext, AudioBuffer, AudioBufferSourceNode}
import scalajs.js.typedarray.ArrayBuffer
import scala.concurrent.{ExecutionContext => EC, _}
import scalajs.js
import scalajs.js.annotation.JSExport

object WebAudioPlayer {
  case class State(
    score: Score,
    tempo: Tempo,
    context: AudioContext,
    buffers: Map[Sample, AudioBuffer],
    playing: Set[NoteOn] = Set.empty,
    sources: Map[Int, AudioBufferSourceNode] = Map.empty,
    stopped: Boolean = false
  ) {
    def stop: State =
      this.copy(stopped = true)
  }

  type Callback = (State, Command) => State

  object Callback {
    val empty: Callback =
      (state, cmd) => state
  }
}

@JSExport
case class WebAudioPlayer(
  sampleUrl: Sample => String,
  context: AudioContext = new AudioContext(),
  callback: WebAudioPlayer.Callback = WebAudioPlayer.Callback.empty
) extends Player[WebAudioPlayer.State] {
  import WebAudioPlayer._

  def initialise(score: Score, tempo: Tempo)(implicit ec: EC): Future[State] = {
    Future.sequence(Compile(score) collect { case NoteOn(_, sample, _) => createBuffer(sample) })
      .map(buffers => State(score, tempo, context, buffers.toMap))
  }

  def playCommand(cmd: Command)(state: State)(implicit ec: EC): Future[State] = {
    if(state.stopped) {
      // Terminate quickly:
      Future.successful(state)
    } else {
      (cmd match {
        case cmd: NoteOn =>
          state.buffers.get(cmd.sample) match {
            case Some(buffer) =>
              Future {
                var source = state.context.createBufferSource()
                source.buffer = buffer
                source.connect(state.context.destination)
                source.playbackRate.setValueAtTime(cmd.pitch.frequency / 220.0, 0)
                source.start(0)
                state.copy(
                  playing = state.playing + cmd,
                  sources = state.sources + (cmd.id -> source)
                )
              }

              case None =>
                Future.successful(state)
          }

        case cmd: NoteOff =>
          state.sources.get(cmd.id).foreach(_.stop(0))
          Future.successful(state.copy(
            playing = state.playing filterNot (_.id == cmd.id),
            sources = state.sources - cmd.id
          ))

        case cmd: Wait =>
          val promise = Promise[State]
          js.timers.setTimeout(state.tempo.milliseconds(cmd.duration)) {
            promise.success(state)
          }
          promise.future
      }) map { state => callback(state, cmd) }
    }
  }

  def createBuffer(sample: Sample)(implicit ec: EC): Future[(Sample, AudioBuffer)] = {
    var request = new XMLHttpRequest()
    request.open("GET", sampleUrl(sample), true)
    request.responseType = "arraybuffer"

    var promise = Promise[(Sample, AudioBuffer)]

    request.onload = (evt: Event) =>
      context.decodeAudioData(
        request.response.asInstanceOf[ArrayBuffer],
        (buffer: AudioBuffer) => promise.success((sample -> buffer))
      )

    request.send()

    promise.future
  }
}
