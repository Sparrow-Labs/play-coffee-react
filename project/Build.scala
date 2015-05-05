import sbt._

object PluginBuild extends Build {

  lazy val playSass = Project(
    id = "play-coffee-react", base = file(".")
  )

}
