package compose.midi

import compose.core._
import java.io.File
import javax.sound.midi.{MidiSystem, Sequence, Track}

object Midi extends EventReducer {
  def read(file: File) =
    readSequence(MidiSystem.getSequence(file))

  def readSequence(sequence: Sequence) =
    sequence.getTracks.toList.map(readTrack)

  def readTrack(track: Track) =
    reduceEvents(readEvents(track))

  def readEvents(track: Track) =
    Iterable.tabulate(track.size)(track.get).flatMap(Event.unapply)

  def main(args: Array[String]): Unit =
    Midi.read(new File("/Users/dave/Desktop/MIDI_sample.mid")).foreach(println)
}
