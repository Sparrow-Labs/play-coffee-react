package de.sparrowlabs

import play.PlayAssetsCompiler
import sbt.Keys._
import sbt._

object CoffeeReactPlugin extends AutoPlugin with PlayAssetsCompiler {
  override def requires = sbt.plugins.JvmPlugin

  override def trigger = allRequirements

  object autoImport {
    val coffeeReactEntryPoints = settingKey[PathFinder]("Paths to CoffeeScript files containing JSX resources to be compiled")
    val coffeeReactOptions = settingKey[Seq[String]]("Command line options for the cjsx command")

    val coffeeReactWatcher = AssetsCompiler("cjsx",
    { file => (file ** "*.cjsx") +++ (file ** "*.cjsx") },
    coffeeReactEntryPoints,
    { (name, min) =>
      name.replace(".cjsx",  if (min) ".min.js" else ".js")
    },
    { (file, options) => CoffeeReactCompiler.compile(file, options) },
    coffeeReactOptions
    )

    lazy val baseCoffeeReactSettings: Seq[Def.Setting[_]] = Seq(
      coffeeReactEntryPoints <<= (sourceDirectory in Compile)(base => ((base / "assets" ** "*.cjsx") +++ (base / "assets" ** "*.cjsx") --- base / "assets" ** "_*")),
      coffeeReactOptions := Seq.empty[String],
      resourceGenerators in Compile <+= coffeeReactWatcher
    )
  }

  import autoImport._

  override val projectSettings = baseCoffeeReactSettings
}
