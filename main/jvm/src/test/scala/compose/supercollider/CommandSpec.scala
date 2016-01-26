package compose.player

import org.scalatest._

class CommandSpec extends FreeSpec with Matchers {
  import Implicits._

  val commands = Vector(
    NoteOn(1, 440),
    Wait(100),
    NoteOff(1),
    Wait(200),
    NoteOn(5, 660),
    Wait(300),
    NoteOff(3)
  )

  val otherCommands = Vector(
    NoteOn(10, 441),
    Wait(150),
    NoteOff(10),
    Wait(50),
    NoteOn(50, 661),
    Wait(50),
    NoteOff(30)
  )

  "Commands.maxChannel should return the maximum channel" in {
    commands.maxChannel should equal(5)
  }

  "Commands.renumberChannels should compress channel numbers down" in {
    commands.renumberChannels(0) should equal(Vector(
      NoteOn(0, 440),
      Wait(100),
      NoteOff(0),
      Wait(200),
      NoteOn(2, 660),
      Wait(300),
      NoteOff(1)
    ))
  }

  "Commands.renumberChannels should use a base index" in {
    commands.renumberChannels(10) should equal(Vector(
      NoteOn(10, 440),
      Wait(100),
      NoteOff(10),
      Wait(200),
      NoteOn(12, 660),
      Wait(300),
      NoteOff(11)
    ))
  }

  "Commands.merge should merge channel sequences" in {
    (commands merge otherCommands) should equal(Vector(
      NoteOn(0,440.0),
      NoteOn(3,441.0),
      Wait(100),
      NoteOff(0),
      Wait(50),
      NoteOff(3),
      Wait(50),
      NoteOn(5,661.0),
      Wait(50),
      NoteOff(4),
      Wait(50),
      NoteOn(2,660.0),
      Wait(300),
      NoteOff(1)
    ))
  }
}
