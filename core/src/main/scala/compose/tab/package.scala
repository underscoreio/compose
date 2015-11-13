package compose

import compose.score.Score

import scala.language.experimental.macros

package object tab {
  implicit class TablatureSyntaxOps(val sc: StringContext) {
    def tab(args: Any*): Score = macro TablatureSyntax.tabMacro
  }
}
