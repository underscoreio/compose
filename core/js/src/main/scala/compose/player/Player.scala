package compose.player

import compose.core._

import scala.concurrent._
import scala.scalajs.js
import org.scalajs.dom

object Player {
  import Implicits._

  type EC = ExecutionContext

  def play(score: Score)(implicit ec: EC): Future[Unit] = {
    val ctx = js.Dynamic.newInstance(js.Dynamic.global.AudioContext)()
    implicit val tempo = 120.bpm

    loadSound(ctx, "web-audio-spike/cat9.wav").flatMap(playCommands(ctx, _, score.compile))
  }

  def loadSound(ctx: js.Dynamic, url: String): Future[js.Dynamic] = {
    var request = new dom.XMLHttpRequest()
    var promise = Promise[js.Dynamic]

    request.open("GET", url, true)
    request.responseType = "arraybuffer"

    request.onload = (evt: dom.Event) =>
      ctx.decodeAudioData(request.response, (buffer: js.Dynamic) => promise success buffer)

    request.send()

    promise.future
  }

  def playCommands(ctx: js.Dynamic, buffer: js.Dynamic, commands: Seq[Command])(implicit ec: EC): Future[Unit] = {
    commands match {
      case Nil =>
        Future.successful(())

      case (head: NoteOn) +: tail =>
        playNoteOn(ctx, buffer, head).flatMap(_ => playCommands(ctx, buffer, tail))

      case (head: Wait) +: tail =>
        playWait(ctx, head).flatMap(_ => playCommands(ctx, buffer, tail))

      case (head: NoteOff) +: tail =>
        playCommands(ctx, buffer, tail)
    }
  }

  def playNoteOn(ctx: js.Dynamic, buffer: js.Dynamic, cmd: NoteOn)(implicit ec: EC): Future[Unit] = {
    Future {
      var source = ctx.createBufferSource()
      source.buffer = buffer
      source.connect(ctx.destination)
      source.playbackRate.setValueAtTime(cmd.freq / 220.0, 0)
      source.start(0)
    }
  }

  def playWait(ctx: js.Dynamic, cmd: Wait)(implicit ec: EC): Future[Unit] = {
    val promise = Promise[Unit]
    dom.setTimeout((() => promise success ()), cmd.millis.toInt)
    promise.future
  }
}

/*
// audiocontext string -> promise(audiobuffer)
function loadSound(ctx, url) {
  var request = new XMLHttpRequest();
  var result = Q.defer();

  request.open('GET', url, true);
  request.responseType = 'arraybuffer';

  request.onload = function() {
    ctx.decodeAudioData(request.response, function(buffer) {
      result.resolve(buffer);
    });
  };

  request.send();

  return result.promise;
}

// audiocontext audiobuffer [double] -> promise(audiobuffer)
function playNoteOn(ctx, buffer, rate) {
  rate = rate || 1.0;

  console.log("playNoteOn", rate);

  var source = ctx.createBufferSource();
  source.buffer = buffer;
  source.connect(ctx.destination);
  window.source = source;
  source.playbackRate.setValueAtTime(rate, 0);
  source.start(0);
}

function playback(ctx, buffer, commands) {
  console.log("playback", commands);

  if(commands.length == 0) {
    return Q.fcall(function() { return "Done"; });
  } else {
    var head = commands[0];
    var tail = commands.slice(1);

    function headPromise() {
      switch(head.type) {
        case "NoteOn":
          console.log("NoteOn");
          return Q.fcall(function() {
            playSound(ctx, buffer, head.rate);
          });

        case "Wait":
          console.log("Wait");
          return Q.delay(head.time);

        default:
          console.log(head.type);
          return Q.fcall(function() {
            return "?!";
          });
      }
    }

    function tailPromise() {
      console.log("tailPromise");
      return playback(ctx, buffer, tail);
    }

    return headPromise().then(tailPromise);
  }
}

function init() {
  var ctx = new AudioContext();
  loadSound(ctx, 'cat9.wav').then(function(meow) {
    playback(ctx, meow, [
      { type: "NoteOn", rate: 1.0 },
      { type: "Wait",  time: 500 },
      { type: "NoteOn", rate: 1.2 },
      { type: "Wait",  time: 500 },
      { type: "NoteOn", rate: 1.4 },
      { type: "Wait",  time: 500 },
      { type: "NoteOn", rate: 1.6 },
      { type: "Wait",  time: 500 },
      { type: "NoteOn", rate: 1.8 }
    ]);
  });
}
*/