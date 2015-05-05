package de.sparrowlabs

import java.io.File

import play.PlayExceptions.AssetCompilationException

import scala.sys.process._

object CoffeeReactCompiler {
  def compile(jsxCoffeeFile: File, opts: Seq[String]): (String, Option[String], Seq[File]) = {
    // Filter out rjs option added by AssetsCompiler until we get clarity on what would
    // be proper solution
    // See: https://groups.google.com/d/topic/play-framework/VbhJUfVl-xE/discussion
    val options = opts.filter { _ != "rjs" }

    try {
      val parentPath = jsxCoffeeFile.getParentFile.getAbsolutePath
      println("Now Compiling: " + jsxCoffeeFile.getAbsolutePath)
      val (jsOutput, dependencies) = runCompiler(
        Seq(cjsxCommand, "-p", "-b") ++ options ++ Seq(jsxCoffeeFile.getAbsolutePath)
      )
      println(options)
      println(jsOutput)
      (jsOutput, Some(""), dependencies.map { new File(_) } )
    } catch {
      case e: CoffeeReactCompilationException => {
        throw AssetCompilationException(e.file.orElse(Some(jsxCoffeeFile)), "[CJSX|" + e.message, Some(e.line), Some(e.column))
      }
    }
  }

  private val cjsxCommand = "cjsx"

  private val DependencyLine = """^/\* line \d+, (.*) \*/$""".r

  private def runCompiler(command: ProcessBuilder): (String, Seq[String]) = {
    println("fuck off dude :) !!!")
    val err = new StringBuilder
    val out = new StringBuilder

    val capturer = ProcessLogger(
      (output: String) => out.append(output + "\n"),
      (error: String)  => err.append(error + "\n"))

    val process = command.run(capturer)
    if (process.exitValue == 0) {
      val dependencies = out.lines.collect {
        case DependencyLine(f) => f
      }

      (out.mkString, dependencies.toList.distinct)
    } else
      throw new CoffeeReactCompilationException(err.toString())
  }

  private val LocationLine = """(.*):(\d+):(\d+): error: (.*)""".r
  private val SyntaxErrorLine = """.*SyntaxError: (.*)""".r
  private class CoffeeReactCompilationException(stderr: String) extends RuntimeException {

    val (file: Option[File], line: Int, column: Int, message: String) = parseError(stderr)

    private def parseError(error: String): (Option[File], Int, Int, String) = {
      var line   = 0
      var seen   = 0
      var column = 0
      var file : Option[File] = None
      var message = "Unknown error, try running cjsx directly"

      for (errline: String <- augmentString(error).lines) {
        errline match {
          case LocationLine(f, l, c, m) =>
            line    = l.toInt
            file    = Some(new File(f))
            column  = c.toInt
            message = "CoffeeScriptError]: " + m
//          case other if seen == 0 =>
//            message = other
//            seen   += 1
          case SyntaxErrorLine(m) =>
            message = "SyntaxError]: " + m
          case _                  => // do nothing
        }
      }

      (file, line, column, message)
    }
  }
}
