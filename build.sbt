scalaVersion := "2.11.5"

scalacOptions += "-feature"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "de.sciss"      %% "scalacollider" % "1.16.0",
  "org.scalatest" %% "scalatest"     % "2.2.1" % "test"
)

