organization  in ThisBuild := "io.underscore"
version       in ThisBuild := "0.5.0"
scalaVersion  in ThisBuild := "2.11.8"

licenses      in ThisBuild += ("Apache-2.0", url("http://apache.org/licenses/LICENSE-2.0"))

scalacOptions in ThisBuild ++= Seq(
  "-feature",
  "-unchecked",
  "-deprecation"
)

pomExtra in Global := {
  <url>https://github.com/underscoreio/compose</url>
  <scm>
    <connection>scm:git:github.com/underscoreio/compose</connection>
    <developerConnection>scm:git:git@github.com:underscoreio/compose</developerConnection>
    <url>github.com/underscoreio/compose</url>
  </scm>
  <developers>
    <developer>
      <id>davegurnell</id>
      <name>Dave Gurnell</name>
      <url>http://twitter.com/davegurnell</url>
    </developer>
  </developers>
}

lazy val noPublishSettings = Seq(
  publish         := {},
  publishArtifact := false
)

lazy val core = crossProject
  .crossType(CrossType.Pure)
  .settings(name := "compose-core")
  .settings(libraryDependencies ++= Seq(
    "org.scala-js"    %% "scalajs-stubs" % scalaJSVersion % Provided,
    "org.scala-lang"   % "scala-reflect" % scalaVersion.value,
    "org.typelevel"  %%% "cats"          % "0.7.0",
    "org.scalatest"   %% "scalatest"     % "2.2.6" % Test
  ))

lazy val coreJVM = core.jvm
lazy val coreJS  = core.js

lazy val examples = crossProject
  .crossType(CrossType.Pure)
  .dependsOn(core)
  .settings(name := "compose-examples")
  .settings(libraryDependencies ++= Seq(
    "org.scala-js"    %% "scalajs-stubs" % scalaJSVersion % Provided
  ))

lazy val examplesJVM = examples.jvm
lazy val examplesJS  = examples.js

lazy val player = crossProject
  .crossType(CrossType.Dummy)
  .dependsOn(core)
  .settings(name := "compose-player")
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats" % "0.7.0"
    )
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "de.sciss"       %% "scalacollider" % "1.20.1",
      "org.scalatest"  %% "scalatest"     % "2.2.6" % Test
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js"  %%% "scalajs-dom" % "0.9.0"
    )
  )

lazy val playerJVM = player.jvm
lazy val playerJS  = player.js

lazy val demo = crossProject
  .crossType(CrossType.Dummy)
  .dependsOn(player, examples)
  .settings(noPublishSettings : _*)
  .settings(name := "compose-demo")
  .jsSettings(
    persistLauncher := true,
    persistLauncher in Test := false
  )
  .jvmSettings(
    initialCommands in console := """
      |import compose.core._
      |import compose.examples._
      |import compose.player._
    """.trim.stripMargin
  )

lazy val demoJVM = demo.jvm
lazy val demoJS  = demo.js

lazy val root = project.in(file("."))
  .aggregate(
    coreJVM,
    coreJS,
    examplesJVM,
    examplesJS,
    playerJVM,
    playerJS,
    demoJVM,
    demoJS
  )
  .settings(noPublishSettings : _*)

run     <<= run     in (demoJVM, Compile)
console <<= console in (demoJVM, Compile)
