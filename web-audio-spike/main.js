window.AudioContext = window.AudioContext || window.webkitAudioContext;

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
function playSound(ctx, buffer, rate) {
  rate = rate || 1.0;

  console.log("playSound", rate);

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

        case "Delay":
          console.log("Delay");
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
      { type: "Delay",  time: 500 },
      { type: "NoteOn", rate: 1.2 },
      { type: "Delay",  time: 500 },
      { type: "NoteOn", rate: 1.4 },
      { type: "Delay",  time: 500 },
      { type: "NoteOn", rate: 1.6 },
      { type: "Delay",  time: 500 },
      { type: "NoteOn", rate: 1.8 }
    ]);
  });
}

window.addEventListener('load', init, false);
