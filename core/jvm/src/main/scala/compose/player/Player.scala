package compose.player

import de.sciss.synth._
import de.sciss.synth.ugen._
import de.sciss.synth.Ops._

import compose.core._

object Player {
  val Freq = "freq"
  val Amp  = "amp"

  def frequency(note: Note): Double =
    math.pow(2, note.value / 12.0) * 440

  def apply(numChannels: Int = 4)(implicit server: Server): Player = {
    val synthDef = SynthDef(s"channel") {
      val freq = Freq.kr(440)
      val amp  = Amp.kr(0.0)
      val osc  = SinOsc.ar(freq, 0.0) * amp
      Out.ar(0, List(osc, osc))
    }

    val channels: Seq[Synth] =
      (0 to numChannels).map(_ => synthDef.play())

    Player(channels.toArray)
  }

  def withPlayer(numChannels: Int)(func: Player => Unit): Unit = {
    Server.run(Server.Config()) { implicit server =>
      val player = Player(numChannels)
      try {
        func(player)
      } finally {
        player.free
      }
    }
  }
}

case class Player(val channels: Array[Synth])(implicit val server: Server) {
  import Player._
  import Implicits._

  val numChannels = channels.length

  def play(cmd: Command): Unit = {
    println(cmd)
    cmd match {
      case NoteOn(num, freq) if num < numChannels => channels(num).set(Amp -> 1.0, Freq -> freq)
      case NoteOff(num)      if num < numChannels => channels(num).set(Amp -> 0.1)
      case Wait(millis)                           => Thread.sleep(millis)
    }
  }

  def play(cmds: Seq[Command]): Unit =
    cmds.foreach(play _)

  def play(score: Score)(implicit tempo: Tempo): Unit =
    play(score.compile)

  def free =
    channels.foreach(_.free)
}
