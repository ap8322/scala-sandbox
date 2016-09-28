import org.scalatest._

class CalcSpec extends FlatSpec with DiagrammedAssertions {
  val calc = new Calc

  "sum function" should "add" in {
    assert(calc.sum(Seq(1,2,3)) === 6)
    assert(calc.sum(Seq(0)) == 0)
    assert(calc.sum(Seq(-1,1)) === 0)
    assert(calc.sum(Seq()) === 0)
  }

  it should "Intの最大を上回った際にはオーバーフローする" in {
    assert(calc.sum(Seq(Integer.MAX_VALUE,1)) === Integer.MIN_VALUE)
  }
}
