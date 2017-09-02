// build.sbt
scalaVersion := "2.11.8"

name := "scala sandbox"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-Xlint")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "mysql" % "mysql-connector-java" % "6.0.6",
  "org.apache.kafka" %% "kafka" % "0.11.0.0"
  //"mysql" % "mysql-connector-java" % "5.1.40"
  //"mysql" % "mysql-connector-java" % "5.1.39"
)

initialCommands in console := "import impl._"

lazy val hello = taskKey[Unit]("An example task")

lazy val root = (project in file(".")).
  settings(
    hello := { println("Hello!") }
  )

