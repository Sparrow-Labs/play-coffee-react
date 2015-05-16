import java.io.File

import de.sparrowlabs.CoffeeReactCompiler
import org.scalatest.FunSpec
import play.PlayExceptions.AssetCompilationException

/**
 * Created by Jovo on 16/05/15.
 */
class CoffeeReactCompilerSpec extends FunSpec {
  describe("CoffeeReactCompiler") {
    it("should compile correct cjsx") {
      val cjsxFile = new File("src/test/resources/correct.cjsx")
      val (full, minified, deps) = CoffeeReactCompiler.compile(cjsxFile, Nil)
      val correctOutput = "var NeatComponent;\n\nNeatComponent = React.createClass({\n  render: function() {\n    var n;\n    return React.createElement(\"div\", {\n      \"className\": \"neat-component\"\n    }, (this.props.showTitle ? React.createElement(\"h1\", null, \"A Component is I\") : void 0), React.createElement(\"hr\", null), (function() {\n      var i, results;\n      results = [];\n      for (n = i = 1; i <= 5; n = ++i) {\n        results.push(React.createElement(\"p\", {\n          \"key\": n\n        }, \"This line has been printed \", n, \" times\"));\n      }\n      return results;\n    })());\n  }\n});\n"
      assert(correctOutput === full)
    }

    it("should throw a CJSX syntax error") {
      val cjsxFile = new File("src/test/resources/cjsx-syntax-error.cjsx")
      try {
        val (full, minified, deps) = CoffeeReactCompiler.compile(cjsxFile, Nil)
      } catch {
        case e: AssetCompilationException => {
          assert(e.message === "[CJSX|SyntaxError]: Unexpected end of input: unclosed CJSX_EL")
        }
      }
    }

    it("should throw a CoffeeScript error") {
      val cjsxFile = new File("src/test/resources/coffeescript-error.cjsx")
      try {
        val (full, minified, deps) = CoffeeReactCompiler.compile(cjsxFile, Nil)
      } catch {
        case e: AssetCompilationException => {
          assert(e.message === "[CJSX|CoffeeScriptError]: unexpected indentation")
        }
      }
    }
  }
}
