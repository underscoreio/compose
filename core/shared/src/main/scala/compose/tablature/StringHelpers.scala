package compose.tablature

trait StringHelpers {
  object StartsWithChar {
    def unapply(str: String) =
      if(str.length == 0) {
        None
      } else {
        Some((str.charAt(0), str.substring(1)))
      }
  }

  object StartsWithFret {
    def unapply(str: String) = {
      if(str.length == 0) {
        None
      } else {
        str.indexWhere(x => !x.isDigit) match {
          case -1 => Some((str.toInt, ""))
          case 0  => None
          case n  => Some((str.substring(0, n).toInt, str.substring(n)))
        }
      }
    }
  }
}