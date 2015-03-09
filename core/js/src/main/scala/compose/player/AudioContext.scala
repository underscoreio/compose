package compose.player

import org.scalajs.dom.raw.Window
import scala.scalajs.js

// Minimal ScalaJS facades for the classes we need from the Web Audio API

class AudioContext extends js.Object {
  def decodeAudioData(data: js.Any, callback: js.Function1[AudioBuffer, _]): Unit = js.native
  def createBufferSource(): AudioSource = js.native
  def destination: AudioDestination = js.native
}

trait AudioDestination extends js.Object

trait AudioBuffer extends js.Object

trait AudioSource extends js.Object {
  var buffer: AudioBuffer = js.native
  def playbackRate: AudioPlaybackRate = js.native
  def connect(des: AudioDestination): Unit = js.native
  def start(time: Int): Unit = js.native
}

trait AudioPlaybackRate extends js.Object {
  def setValueAtTime(rate: Double, time: Double): Unit = js.native
}