package compose.examples

import compose.core._
import compose.core.Pitch._
import compose.core.{Rest => r}
import scalajs.js.annotation.JSExport

trait Freebird {
  import compose.tablature._

  val rhythm1 =
    (G3.q | D4.q | G4.q) ~
    r.e ~
    (G3.e | D4.e | G4.e) ~
    (G3.q | D4.q | G4.q) ~
    r.e ~
    (As3.e | F4.e | As4.e) ~
    r.e ~
    (As3.e | F4.e | As4.e) ~
    r.e ~
    (As3.e | F4.e | As4.e) ~
    (As3.q | F4.q | As4.q) ~
    r.e ~
    r.e ~
    (C4.q | G4.q | C5.q) ~
    r.e ~
    (C4.e | G4.e | C5.e) ~
    (C4.q | G4.q | C5.q) ~
    r.e ~
    (C4.e | G4.e | C5.e) ~
    r.e ~
    (C4.e | G4.e | C5.e) ~
    r.e ~
    (C4.e | G4.e | C5.e) ~
    (C4.q | G4.q | C5.q) ~
    r.e ~
    r.e

  val rhythm2 =
    (G3.q | D4.q | G4.q) ~
    r.e ~
    (G3.e | D4.e | G4.e) ~
    (G3.q | D4.q | G4.q) ~
    r.e ~
    (As3.e | F4.e | As4.e) ~
    r.e ~
    (As3.e | F4.e | As4.e) ~
    r.e ~
    (As3.e | F4.e | As4.e) ~
    (As3.q | F4.q | As4.q) ~
    r.e ~
    r.e ~
    (C4.q | G5.q | C5.q) ~
    r.e ~
    (C4.e | G5.e | C5.e) ~
    (C4.q | G5.q | C5.q) ~
    r.e ~
    (C4.e | G5.e | C5.e) ~
    r.e ~
    (Ds3.e | As4.e | Ds4.e) ~
    r.e ~
    (E3.e | B4.e | E4.e) ~
    r.e ~
    (F3.e | C4.e | F4.e) ~
    r.e ~
    (Fs3.e | Cs4.e | Fs4.e)

  val solo0 =
    (r.w * 3) ~
    (r.e * 6) ~
    F5.e ~
    D5.e

  val solo1 =
    G5.q ~
    G5.q ~
    G5.q ~
    F5.e ~
    D5.e ~
    F5.e ~
    G5.e ~
    F5.e ~
    D5.e ~
    G5.q ~
    F5.e ~
    D5.e ~
    G5.e ~
    G5.q ~
    (As5.q | (r.e ~ G5.q)) ~
    (As5.q | (r.e ~ G5.q)) ~
    (As5.q | (r.e ~ G5.q)) ~
    G5.q ~
    F5.e ~
    D5.e

  val solo2 =
    G5.q ~
    G5.q ~
    G5.q ~
    F5.e ~
    D5.e ~
    F5.e ~
    G5.e ~
    F5.e ~
    D5.e ~
    G5.q ~
    F5.e ~
    D5.e ~
    G5.e ~
    G5.q ~
    (As5.q | (r.e ~ G5.q)) ~
    (As5.q | (r.e ~ G5.q)) ~
    (As5.e | G5.e) ~
    (As5.q | G5.q) ~
    (As5.q | G5.q) ~
    (As5.q | G5.q)

  val solo3 =
    (As5.q | G5.q) ~
    (As5.q | G5.q) ~
    (As5.q | G5.q) ~
    (As5.q | G5.q) ~
    (As5.q | G5.q) ~
    (As5.q | G5.q) ~
    (As5.q | G5.q) ~
    (As5.q | G5.q) ~
    (As5.s | G5.s) ~
    r.s ~
    (As5.q | G5.q) ~
    (As5.s | G5.s) ~
    r.s ~
    (As5.q | G5.q) ~
    (As5.s | G5.s) ~
    r.s ~
    (As5.q | G5.q) ~
    (As5.s | G5.s) ~
    r.s ~
    (As5.q | G5.q) ~
    (As5.h | G5.h)

  val solo4 =
    As5.s ~ G5.s ~ As5.s ~ G5.s ~ F5.s ~ D5.s ~
    As5.s ~ G5.s ~ As5.s ~ G5.s ~ F5.s ~ D5.s ~
    As5.s ~ G5.s ~ As5.s ~ G5.s ~ F5.s ~ D5.s ~
    As5.s ~ G5.s ~ As5.s ~ G5.s ~ F5.s ~ D5.s ~
    As5.s ~ G5.s ~ F5.s  ~ D5.s ~
    As5.s ~ G5.s ~ F5.s  ~ D5.s ~
    (C5.q repeat 4) ~
    C5.e ~
    Ds4.e ~ r.e ~
    E4.e ~ r.e ~
    F4.e ~ r.e ~
    Fs4.e

  val end =
    (G3.q | D4.q | G4.q) ~
    (G3.q | D4.q | G4.q)

  // Pilfered from https://tabs.ultimate-guitar.com/l/lynyrd_skynyrd/free_bird_solo_tab.htm
  @JSExport val freebird =
    (rhythm1 | solo0) ~
    (rhythm1 | solo1) ~
    (rhythm2 | solo2) ~
    (rhythm1 | solo3) ~
    (rhythm2 | solo4) ~
    end
}
