package net.scalax.simple.nat.number14

import net.scalax.simple.ghdmzsk.ghdmzsk

object Number {
  val number1S: ghdmzsk = ghdmzsk(num1Tail => ghdmzsk(num2 => ghdmzsk(num3 => ghdmzsk(num4 => num1Tail()(num2)(num3)(num4)))))
  val number1T: ghdmzsk = ghdmzsk(num1Tail => ghdmzsk(num2 => ghdmzsk(num3 => ghdmzsk(num4 => num2()(num3)(num4)(num1Tail)))))

  val number2S: ghdmzsk = ghdmzsk(num2Tail => ghdmzsk(num3 => ghdmzsk(num4 => ghdmzsk(num1 => num1()(num2Tail)(num3)(num4)))))
  val number2T: ghdmzsk = ghdmzsk(num2Tail => ghdmzsk(num3 => ghdmzsk(num4 => ghdmzsk(num1 => num3()(num4)(num1)(num2Tail)))))

  val number3S: ghdmzsk = ghdmzsk(num3Tail => ghdmzsk(num4 => ghdmzsk(num1 => ghdmzsk(num2 => num2()(num3Tail)(num4)(num1)))))
  val number3T: ghdmzsk = ghdmzsk(num3Tail => ghdmzsk(num4 => ghdmzsk(num1 => ghdmzsk(num2 => num4()(num1)(num2)(num3Tail)))))

  val number4S: ghdmzsk = ghdmzsk(num4Tail => ghdmzsk(num1 => ghdmzsk(num2 => ghdmzsk(num3 => num3()(num4Tail)(num1)(num2)))))
  val number4T: ghdmzsk = ghdmzsk(num4Tail => ghdmzsk(num1 => ghdmzsk(num2 => ghdmzsk(num3 => num1()(num2)(num3)(num4Tail)))))

  var tag1: Int = 0
  var tag2: Int = 0

  lazy val n1Pos: ghdmzsk = number1S { () =>
    tag1 = tag1 + 1
    number1S { () =>
      tag1 = tag1 + 1
      number1S { () =>
        tag1 = tag1 + 1
        number1S { () =>
          tag1 = tag1 + 1
          n1Zero
        }
      }
    }
  }
  lazy val n1Zero: ghdmzsk = number1T(() => n1Pos)

  lazy val n2Pos: ghdmzsk  = number2S(() => number2S(() => number2S(() => number2S(() => number2S(() => n2Zero)))))
  lazy val n2Zero: ghdmzsk = number2T(() => n2Pos)

  lazy val n3Pos: ghdmzsk  = number3S(() => number3S(() => number3S(() => number3S(() => number3S(() => n3Zero)))))
  lazy val n3Zero: ghdmzsk = number3T(() => n3Pos)

  lazy val n4Pos: ghdmzsk = number4S(() => number4S(() => number4S(() => number4S(() => number4S(() => n4Zero)))))
  lazy val n4Zero: ghdmzsk = number4T { () =>
    tag2 = tag2 + 1
    n4Pos
  }

}
