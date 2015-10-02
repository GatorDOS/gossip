EclipseKeys.withSource := true
name := "Gossip"

version := "1.0"

scalaVersion := "2.11.6"
val akkaVersion = "2.3.13"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
