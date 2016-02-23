package compose.player

import compose.core._
import de.sciss.synth._
import de.sciss.synth.ugen.{ Pitch => _, _ }
import de.sciss.synth.Ops._
import java.util.{Timer, TimerTask}
import scala.concurrent.{ExecutionContext => EC, _}

object ScPlayer {
  val Freq = "freq"
  val Amp  = "amp"

  val sine = SynthDef(s"sine") {
    val freq = Freq.kr(440)
    val amp  = Amp.kr(0.0)
    val osc  = SinOsc.ar(freq, 0.0) * amp
    Out.ar(0, List(osc, osc))
  }

  def withPlayer[A](numChannels: Int = 4, synthDef: SynthDef = sine)(func: ScPlayer => A) =
    Server.run { server =>
      val player = new ScPlayer(numChannels, synthDef, server)
      try {
        func(player)
      } finally {
        player.free
      }
    }

  case class State(available: Seq[Synth], playing: Map[Int, Synth])
}

class ScPlayer(numChannels: Int, synthDef: SynthDef, server: Server) extends Player[ScPlayer.State] {
  import ScPlayer._
  import Command._

  val timer = new Timer()
  val channels = (0 to numChannels).map(_ => synthDef.play())

  def free: Unit =
    channels.foreach(_.free)

  def initialise: Future[State] =
    Future.successful(State(channels, Map.empty))

  override def shutdown(state: State): Future[State] =
    Future.successful(State(Nil, Map.empty))

  def playCommand(state: State, cmd: Command)(implicit ec: EC, tempo: Tempo): Future[State] = {
    cmd match {
      case NoteOn(id, pitch) =>
        state.available match {
          case Seq() => Future.successful(state)

          case channel +: remaining =>
            channel.set(Amp -> 1.0, Freq -> pitch.frequency)
            Future.successful(State(remaining, state.playing + (id -> channel)))
        }

      case NoteOff(id) =>
        state.playing.get(id) match {
          case Some(channel) =>
            channel.set(Amp -> 0.1)
            Future.successful(State(state.available :+ channel, state.playing - id))
        }

      case Wait(dur) =>
        val promise = Promise[State]
        val task = new TimerTask {
          def run(): Unit = promise.success(state)
        }
        timer.schedule(task, tempo.milliseconds(dur))
        promise.future
    }
  }
}
