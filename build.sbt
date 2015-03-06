lazy val core = crossProject.
  crossType(CrossType.Full).
  settings(
    name          := "compose-core",
    organization  := "io.underscore",
    scalaVersion  := "2.11.5"
  ).jvmSettings(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "de.sciss"      %% "scalacollider" % "1.16.0",
      "org.scalatest" %% "scalatest"     % "2.2.1" % "test"
    )
  ).jsSettings(
  )

lazy val coreJVM = core.jvm

lazy val coreJS  = core.js

lazy val main = crossProject.
  crossType(CrossType.Full).
  dependsOn(core).
  settings(
    name          := "compose",
    organization  := "io.underscore",
    scalaVersion  := "2.11.5",
    scalacOptions += "-feature"
  ).jvmSettings(
    initialCommands in console := """
      import compose._
    """.trim.stripMargin
  ).jsSettings(
  )

lazy val mainJVM = main.jvm

lazy val mainJS  = main.js

// // Handy shortcuts:
// //  - `run`       runs `mainJVM/run`
// //  - `console`   runs `mainJVM/console`

// run     <<= run     in (mainJVM, Compile)

// console <<= console in (mainJVM, Compile)
