organization in ThisBuild := "com.davegurnell"
version      in ThisBuild := "0.1.0"
scalaVersion in ThisBuild := "2.11.7"

lazy val core = crossProject.
  crossType(CrossType.Full).
  settings(
    name                 := "compose-core",
    bintrayOrganization  := Some("davegurnell"),
    bintrayRepository    := "maven",
    licenses             += ("Apache-2.0", url("http://apache.org/licenses/LICENSE-2.0")),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats"          % "0.4.1",
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.scalatest" %% "scalatest"     % "2.2.6" % Test
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
      "org.typelevel" %%% "cats"         % "0.4.1",
      "org.scala-js"  %%% "scalajs-dom"  % "0.9.0"
    )
  )

lazy val coreJVM = core.jvm
lazy val coreJS  = core.js

lazy val main = crossProject
  .crossType(CrossType.Full)
  .dependsOn(core)
  .settings(
    name := "compose",
    scalacOptions += "-feature",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.6" % "test"
    )
  )
  .jvmSettings(
    initialCommands in console := "import compose._"
  )
  .jsSettings(
    persistLauncher := true,
    persistLauncher in Test := false
  )

lazy val mainJVM = main.jvm
lazy val mainJS  = main.js

run     <<= run     in (mainJVM, Compile)
console <<= console in (mainJVM, Compile)
