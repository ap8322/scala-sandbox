class Calc {

  /** 整数の配列を取得し、それらを出し合わせた整数を返す
    *
    * Intの最大を上回った際にはオーバーフローする
    */
  def sum(seq: Seq[Int]): Int = seq.sum

  /** 整数を2つ受け取り、分子を分母で割った浮動小数点の値を返す
    *
    * 0で割ろうとした際には実行時例外が投げられる
    */
  def div(numerator: Int, denominator: Int): Double = {
    if (denominator == 0) throw new ArithmeticException("/ by zero")
    numerator.toDouble / denominator.toDouble
  }

  /** 整数値を一つ受け取り、その値が素数であるかどうかのブール値を返す */
  def isPrime(n: Int): Boolean = {
    if (n < 2) false else !((2 to Math.sqrt(n).toInt) exists (n % _ == 0))
  }
}
