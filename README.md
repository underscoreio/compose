# Compose

A compositional music composition library.

Copyright 2015-2016 [Dave Gurnell][davegurnell]. Licensed [Apache 2][license].

[![Build Status](https://travis-ci.org/underscoreio/compose.svg?branch=develop)][travis]
[![Coverage status](https://img.shields.io/codecov/c/github/underscoreio/compose/develop.svg)](https://codecov.io/github/underscoreio/compose)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.underscore/compose_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.underscore/compose_2.11)
[![Join the chat at https://gitter.im/underscoreio/scala](https://badges.gitter.im/underscoreio/scala.svg)][gitter]

# Overview

*Compose* is a library for writing songs using functional programming.
It is split into a number of subprojects:

- `compose-core`     - data structures and DSLs for building songs;
- `compose-examples` - example songs written using `compose-core`;
- `compose-player`   - players for the JVM and ScalaJS;
- `compose-demo`     - local project for quick experimentation (not published to Bintray).

You can read more about Compose on the [Underscore blog][blog].

Compose is cross-built for the JVM and ScalaJS.
The JVM player uses [ScalaCollider][scalacollider] and [Supercollider][supercollider]
to play songs using a synthesizer (a sine wave by default).
The ScalaJS player uses the [web audio API][webaudio] to play songs using samples.

# Quick Start

If you're working on the JVM, you can add Compose to your SBT build as follows:

```scala
libraryDependencies ++= Seq(
  "io.underscore" %% "compose-core"     % "<<VERSION>>",
  "io.underscore" %% "compose-player"   % "<<VERSION>>",
  "io.underscore" %% "compose-examples" % "<<VERSION>>"
)
```

If you're working in ScalaJS, use the following settings instead:

```scala
libraryDependencies ++= Seq(
  "io.underscore" %%% "compose-core"     % "<<VERSION>>",
  "io.underscore" %%% "compose-player"   % "<<VERSION>>",
  "io.underscore" %%% "compose-examples" % "<<VERSION>>"
)
```

Once you've added Compose to your build, you can write songs as follows:

```scala
import compose.core._

val song =
  Note(Pitch.C3, Duration.Quarter) ~
  Note(Pitch.E3, Duration.Quarter) ~
  Note(Pitch.G3, Duration.Quarter) ~
  Note(Pitch.G3, Duration.Whole)
```

There are numerous shortcuts and conversions to make this code easier to write.
Check out the [examples][examples] for inspiration.

You can play your song on the JVM as follows:

```scala
import compose.player._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{ Duration => Dur }

// Create a player:
ScalaColliderPlayer.withPlayer(4) { player: ScalaColliderPlayer =>

  // Start the song playing:
  val playing: Future[ScalaColliderPlayer.State] =
    player.play(song, Tempo(180))

  // Wait for the song to finish:
  Await.result(playing, Dur.Inf)

}
```

On ScalaJS, the code looks like the following:

```scala
import compose.player._
import scala.concurrent.Await
import scala.concurrent.duration.{ Duration => Dur }
import scalajs.concurrent.JSExecutionContext.Implicits.queue

// Create a player:
val player: WebAudioPlayer = new WebAudioPlayer()

// Start the song playing:
val playing: Future[WebAudioPlayer.State] =
  player.play(song, Tempo(180))

// Wait for the song to finish:
Await.result(playing, Dur.Inf)
```

That's all. If you have any questions, please ask on [Gitter][gitter]. Happy composing!

[blog]: http://underscore.io/blog/posts/2015/03/05/compositional-music-composition.html
[davegurnell]: http://davegurnell.com
[examples]: https://github.com/underscoreio/compose/blob/develop/main/shared/src/main/scala/compose/examples
[gitter]: https://gitter.im/underscoreio/scala?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge
[license]: http://www.apache.org/licenses/LICENSE-2.0.txt
[scalacollider]: https://github.com/Sciss/ScalaCollider
[supercollider]: http://www.audiosynth.com
[travis]: https://travis-ci.org/underscoreio/compose
[webaudio]: https://developer.mozilla.org/en-US/docs/Web/API/Web_Audio_API
