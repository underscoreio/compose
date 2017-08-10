package compose.player

import compose.core._
import javax.sound.midi._
import java.util.{ Timer, TimerTask }
import scala.concurrent.{ ExecutionContext => EC, _ }

object JavaSoundPlayer {
  def withPlayer[A]()(func: JavaSoundPlayer => A) = {
    val player = new JavaSoundPlayer(MidiSystem.getSynthesizer)
    try {
      func(player)
    } finally {
      player.free
    }
  }

  case class State(events: Seq[MidiEvent], currentTick: Long, playing: Map[Int, Pitch], tempo: Tempo)
}

class JavaSoundPlayer(synth: Synthesizer) extends Player[JavaSoundPlayer.State] {
  import JavaSoundPlayer._
  import Command._

  val NOTE_ON_VELOCITY = 100
  val NOTE_OFF_VELOCITY = 100
  val MIDI_CONCERT_A = 69
  val PAD_TIME = 1000 // extra time in msec to wait after sequence plays

  def free: Unit =
    synth.close()

  def initialise: Future[State] =
    Future.successful(State(Seq(), 1, Map.empty, Tempo()))

  override def shutdown(state: State): Future[State] = {
    // Durations are measured in 64th notes, so 16 divisions per beat (quarter note)
    val sequence = new Sequence(Sequence.PPQ, 16)
    val track = sequence.createTrack()
    state.events foreach { track.add _ }

    val sequencer = MidiSystem.getSequencer
    if (sequencer == null) return Future.failed(new Exception("Unable to get MIDI sequencer"))

    try {
      sequencer.open()
      synth.open()
    } catch {
      case e: MidiUnavailableException => return Future.failed(e)
    }

    sequencer.setSequence(sequence)
    sequencer.setTempoInBPM(state.tempo.bpm)
    sequencer.getTransmitter.setReceiver(synth.getReceiver)

    val timer = new Timer()
    val promise = Promise[State]
    val task = new TimerTask {
      def run(): Unit = {
        sequencer.stop()
        sequencer.close()
        promise.success(state)
      }
    }
    implicit val tempo = state.tempo
    timer.schedule(task, PAD_TIME + tempo.milliseconds(Duration(state.currentTick.toInt)))
    sequencer.start()
    promise.future
  }

  def playCommand(state: State, cmd: Command)(implicit ec: EC, tempo: Tempo): Future[State] = {
    cmd match {
      case NoteOn(id, pitch) =>
        val updated = state.playing + (id -> pitch)
        
        if (state.playing.values.toSeq contains pitch) {
          // Ignore note on -- already playing
          Future.successful(State(state.events, state.currentTick, updated, tempo))
        } else {
          val midiPitch = pitch.value + MIDI_CONCERT_A
          val midiVelocity = NOTE_ON_VELOCITY
          val event = new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON, midiPitch, midiVelocity), state.currentTick)
          Future.successful(State(state.events :+ event, state.currentTick, updated, tempo))
        }

      case NoteOff(id) =>
        val pitch = state.playing(id)
        val updated = state.playing - id
        
        if (updated.values.toSeq contains pitch) {
          // Ignore note off -- still playing
          Future.successful(State(state.events, state.currentTick, updated, tempo))
        } else {
          val midiPitch = pitch.value + MIDI_CONCERT_A
          val midiVelocity = NOTE_OFF_VELOCITY
          val event = new MidiEvent(new ShortMessage(ShortMessage.NOTE_OFF, midiPitch, midiVelocity), state.currentTick)
          Future.successful(State(state.events :+ event, state.currentTick, updated, tempo))
        }

      case Wait(dur) =>
        Future.successful(State(state.events, state.currentTick + dur.value, state.playing, tempo))
    }
  }
}
