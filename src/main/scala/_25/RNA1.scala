package _25

class RNA1 private (val groups: Array[Int],val length: Int) extends IndexedSeq[Base]{
  import RNA1._
  def apply(idx: Int): Base = {
    if (idx < 0 || length <= idx) throw new IndexOutOfBoundsException
    Base.formInt(groups(idx / n) >> (idx % N * S) & M)
  }
}

object RNA1 {
  private val S = 2
  private val N = 32 / S
  private val M = (1 << S) - 1
  def fromSeq(buf: Seq[Base]): RNA1 = {
    val groups = new Array[Int]((buf.length + N - 1) / N)
    for (i <- buf.indices)
      groups(i / N) |= Base.toInt(buf(i)) << (i % N * S)
    new RNA1(groups,buf.length)
  }
  def apply(bases: Base*) = fromSeq(bases)
}
