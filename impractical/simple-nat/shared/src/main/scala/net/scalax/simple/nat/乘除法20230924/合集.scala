package net.scalax.simple.nat.合集1

object 合集 {

  trait CLeft
  trait CRight

  // ResultAB
  abstract class NumCount(tail: () => NumCount)
  case class NumCountLeft(tail: () => NumCount)  extends NumCount(tail) with CLeft
  case class NumCountRight(tail: () => NumCount) extends NumCount(tail) with CRight

  // NUMAB
  abstract class InputNum(tail: () => InputNum) extends NumCount(tail) {
    def input(num2: InputNum, num3: InputNum): NumCount
  }

  case class InputNum1(num1: () => InputNum) extends InputNum(num1) with CLeft {
    def input(num2: InputNum, num3: InputNum): NumCount = NumCountLeft(() => num1().input(num2, num3))
  }

  case class InputNum2(num1: () => InputNum) extends InputNum(num1) with CRight {
    def input(num2: InputNum, num3: InputNum): NumCount = NumCountRight(() => num1().input(num2, num3))
  }

  case class InputNum3(num1: () => InputNum) extends InputNum(num1) with CLeft {
    def input(num2: InputNum, num3: InputNum): NumCount = num3.input(num2, num1())
  }

  case class InputNum4(num1: () => InputNum) extends InputNum(num1) with CRight {
    def input(num2: InputNum, num3: InputNum): NumCount = num2.input(num1(), num3)
  }

  case class InputNum5(num1: () => InputNum) extends InputNum(num1) with CLeft {
    def input(num2: InputNum, num3: InputNum): NumCount = num2.input(num1(), num3)
  }

  case class InputNum6(num1: () => InputNum) extends InputNum(num1) with CRight {
    def input(num2: InputNum, num3: InputNum): NumCount = num3.input(num2, num1())
  }

}
