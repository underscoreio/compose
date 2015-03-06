package compose

import compose.core.Score
import scala.language.experimental.macros

package object tablature {
  implicit class TablatureSyntaxOps(val sc: StringContext) {
    def tab(args: Any*): Score = macro TablatureSyntax.tabMacro
  }
}
