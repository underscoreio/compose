organization in ThisBuild := "io.underscore"
scalaVersion in ThisBuild := "2.11.7"

lazy val bintraySettings = Seq(
  bintrayOrganization := Some("underscoreio"),
  bintrayRepository   := "training",
  licenses            += ("Apache-2.0", url("http://apache.org/licenses/LICENSE-2.0"))
)

lazy val noPublishSettings = Seq(
  publish := {}
)

lazy val core = crossProject
  .crossType(CrossType.Pure)
  .settings(name := "compose-core")
  .settings(bintraySettings : _*)
  .settings(libraryDependencies ++= Seq(
    "org.typelevel" %% "cats"          % "0.4.1",
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.scalatest" %% "scalatest"     % "2.2.6" % Test
  ))

lazy val coreJVM = core.jvm
lazy val coreJS  = core.js

lazy val examples = crossProject
  .crossType(CrossType.Pure)
  .dependsOn(core)
  .settings(name := "compose-examples")
  .settings(bintraySettings : _*)

lazy val examplesJVM = examples.jvm
lazy val examplesJS  = examples.js

lazy val player = crossProject
  .crossType(CrossType.Dummy)
  .dependsOn(core)
  .settings(name := "compose-player")
  .settings(bintraySettings : _*)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats" % "0.4.1"
    )
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "de.sciss"      %% "scalacollider" % "1.16.0",
      "org.scalatest" %% "scalatest"     % "2.2.6" % Test
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
    """.trim.stripMargin,
    fork in run := true,
    connectInput in run := true
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
