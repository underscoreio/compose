package compose.player

import compose.core._
import de.sciss.synth._
import de.sciss.synth.ugen.{ Pitch => _, _ }
import de.sciss.synth.Ops._
import java.util.{Timer, TimerTask}
import scala.concurrent.{ExecutionContext => EC, _}

object ScalaColliderPlayer {
  val Freq = "freq"
  val Amp  = "amp"

  val sine = SynthDef("sine") {
    val freq = Freq.kr(440)
    val amp  = Amp.kr(0.0)
    val osc  = SinOsc.ar(freq, 0.0) * amp
    Out.ar(0, List(osc, osc))
  }

  def withPlayer[A](numChannels: Int = 4, synthDef: SynthDef = sine)(func: ScalaColliderPlayer => A) =
    Server.run { server =>
      val player = new ScalaColliderPlayer(numChannels, synthDef, server)
      try {
        func(player)
      } finally {
        player.free
      }
    }

  case class State(
    score     : Score,
    tempo     : Tempo,
    available : Seq[Synth],
    playing   : Map[Int, Synth]
  )
}

class ScalaColliderPlayer(numChannels: Int, synthDef: SynthDef, server: Server) extends Player[ScalaColliderPlayer.State] {
  import ScalaColliderPlayer._
  import Command._

  val timer = new Timer()
  val channels = (0 to numChannels).map(_ => synthDef.play())

  def free: Unit =
    channels.foreach(_.free)

  def initialise(score: Score, tempo: Tempo)(implicit ec: EC): Future[State] =
    Future.successful(State(score, tempo, channels, Map.empty))

  override def shutdown(state: State)(implicit ec: EC): Future[State] =
    Future.successful(state.copy(available = Nil, playing = Map.empty))

  def playCommand(cmd: Command)(state: State)(implicit ec: EC): Future[State] = {
    cmd match {
      case NoteOn(id, _, pitch) =>
        state.available match {
          case Seq() => Future.successful(state)

          case channel +: remaining =>
            channel.set(Amp -> 1.0, Freq -> pitch.frequency)
            Future.successful(state.copy(available = remaining, playing = state.playing + (id -> channel)))
        }

      case NoteOff(id) =>
        state.playing.get(id) match {
          case Some(channel) =>
            channel.set(Amp -> 0.001)
            Future.successful(state.copy(available = state.available :+ channel, playing = state.playing - id))

          case None =>
            Future.successful(state)
        }

      case Wait(dur) =>
        val promise = Promise[State]
        val task = new TimerTask {
          def run(): Unit = promise.success(state)
        }
        timer.schedule(task, state.tempo.milliseconds(dur))
        promise.future
    }
  }
}
