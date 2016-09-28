package compose.midi

import compose.core._
import javax.sound.midi.{ShortMessage, MidiEvent => JavaMidiEvent}

/** A MIDI event (note on or note off) */
sealed trait MidiEvent {
  def tick: Long
  def pitch: Pitch
}

case class NoteOn(tick: Long, pitch: Pitch) extends MidiEvent
case class NoteOff(tick: Long, pitch: Pitch) extends MidiEvent

object Event {
  val NOTE_ON  = 0x90
  val NOTE_OFF = 0x80

  def unapply(event: JavaMidiEvent): Option[MidiEvent] =
    event.getMessage match {
      case msg: ShortMessage =>
        msg.getCommand match {
          case NOTE_ON  => Some(NoteOn(event.getTick, pitch(msg.getData1)))
          case NOTE_OFF => Some(NoteOff(event.getTick, pitch(msg.getData1)))
          case _ => None
        }
      case _ => None
    }

  implicit val ordering: Ordering[MidiEvent] =
    Ordering.by(_.tick)

  def pitch(value: Int): Pitch = {
    val octave = (value / 12) - 1
    val note   = (value % 12)
    Pitch(octave, note)
  }
}
