// build.sbt
scalaVersion := "2.11.8"

name := "scala sandbox"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "mysql" % "mysql-connector-java" % "6.0.6"
)

lazy val hello = taskKey[Unit]("An example task")

lazy val root = (project in file(".")).
  settings(
    hello := { println("Hello!") }
  )
