/**
  * Created by yuki on 2016/10/06.
  */
object MaxList {
  def maxListImpParam[T](elem: List[T])(implicit orderer: T => Ordered[T]): T = {
    elem match {
      case List() => throw new IllegalArgumentException("empty list")
      case List(x) => x
      case x :: rest =>
        val max = maxListImpParam(rest)(orderer)
        if (orderer(x) > max) x
        else max
    }
  }
}
