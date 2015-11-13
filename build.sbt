scalaVersion in ThisBuild := "2.11.7"

scalacOptions in ThisBuild += "-feature"

lazy val core = project.in(file("core"))
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "de.sciss"      %% "scalacollider" % "1.17.4",
      "org.scalatest" %% "scalatest"     % "2.2.5" % "test"
    )
  )

lazy val root = project.in(file(".")).dependsOn(core)
