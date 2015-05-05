name := "play-coffee-react"

version := "0.0.2"

sbtPlugin := true

organization := "de.sparrow-labs"

description := "SBT plugin for handling react.js assets written in CoffeeScript in Play 2.3"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

/// Dependencies

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test"
)

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.1")

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

useGpg:= true

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/Sparrow-Labs/play-coffee-react/</url>
  <licenses>
    <license>
      <name>MIT-style</name>
      <url>https://github.com/Sparrow-Labs/play-coffee-react/blob/master/LICENSE</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:Sparrow-Labs/play-coffee-react.git</url>
    <connection>scm:git:git@github.com:Sparrow-Labs/play-coffee-react.git</connection>
  </scm>
  <developers>
    <developer>
      <id>ji</id>
      <name>Yovoslav Ivanov</name>
      <url>https://github.com/ji</url>
    </developer>
  </developers>
)
