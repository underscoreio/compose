package compose.player

import org.scalatest._

class CommandSpec extends FreeSpec with Matchers {
  import Implicits._

  val commands = Vector(
    PitchOn(1, 440),
    Wait(100),
    PitchOff(1),
    Wait(200),
    PitchOn(5, 660),
    Wait(300),
    PitchOff(3)
  )

  val otherCommands = Vector(
    PitchOn(10, 441),
    Wait(150),
    PitchOff(10),
    Wait(50),
    PitchOn(50, 661),
    Wait(50),
    PitchOff(30)
  )

  "Commands.maxChannel should return the maximum channel" in {
    commands.maxChannel should equal(5)
  }

  "Commands.renumberChannels should compress channel numbers down" in {
    commands.renumberChannels(0) should equal(Vector(
      PitchOn(0, 440),
      Wait(100),
      PitchOff(0),
      Wait(200),
      PitchOn(2, 660),
      Wait(300),
      PitchOff(1)
    ))
  }

  "Commands.renumberChannels should use a base index" in {
    commands.renumberChannels(10) should equal(Vector(
      PitchOn(10, 440),
      Wait(100),
      PitchOff(10),
      Wait(200),
      PitchOn(12, 660),
      Wait(300),
      PitchOff(11)
    ))
  }

  "Commands.merge should merge channel sequences" in {
    (commands merge otherCommands) should equal(Vector(
      PitchOn(0,440.0),
      PitchOn(3,441.0),
      Wait(100),
      PitchOff(0),
      Wait(50),
      PitchOff(3),
      Wait(50),
      PitchOn(5,661.0),
      Wait(50),
      PitchOff(4),
      Wait(50),
      PitchOn(2,660.0),
      Wait(300),
      PitchOff(1)
    ))
  }
}
