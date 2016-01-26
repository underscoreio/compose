lazy val core = crossProject.
  crossType(CrossType.Full).
  settings(
    name                 := "compose-core",
    organization         := "io.underscore",
    scalaVersion         := "2.11.7",
    libraryDependencies  += "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ).jvmSettings(
    libraryDependencies += "de.sciss"       %% "scalacollider" % "1.16.0",
    libraryDependencies += "org.scalatest"  %% "scalatest"     % "2.2.6" % "test"
  ).jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.1"
    )
  )

lazy val coreJVM = core.jvm

lazy val coreJS  = core.js

lazy val main = crossProject.
  crossType(CrossType.Full).
  dependsOn(core).
  settings(
    name                := "compose",
    organization        := "io.underscore",
    scalaVersion        := "2.11.7",
    scalacOptions       += "-feature",
    libraryDependencies += "org.scalatest"  %% "scalatest"     % "2.2.6" % "test"
  ).jvmSettings(
    initialCommands in console := """
      import compose._
    """.trim.stripMargin
  ).jsSettings(
    persistLauncher         := true,
    persistLauncher in Test := false
  )

lazy val mainJVM = main.jvm

lazy val mainJS  = main.js

run     <<= run     in (mainJVM, Compile)

console <<= console in (mainJVM, Compile)
