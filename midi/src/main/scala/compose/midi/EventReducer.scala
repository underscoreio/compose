package compose.midi

import compose.core._

trait EventReducer {
  case class Seed(
    playing : Map[Pitch, Long] = Map.empty,
    score  : Score            = EmptyScore
  )

  def reduceEvents(events: Iterable[MidiEvent]): Score =
    events.foldLeft(Seed()) { (seed, event) =>
      event match {
        case NoteOn(start, pitch) =>
          seed.playing.get(pitch) match {
            case Some(start) =>
              seed

            case None =>
              seed.copy(seed.playing + (pitch -> start))
          }

        case NoteOff(end, pitch) =>
          seed.playing.get(pitch) match {
            case Some(start) =>
              val score = Rest(dur(start)) ~ Note(pitch, dur(end - start))
              seed.copy(seed.playing - pitch, seed.score | score)

            case None =>
              seed.copy(seed.playing - pitch)
          }
      }
    }.score

  def dur(tick: Long): Duration =
    Duration(tick.toInt)
}