package impl
import scala.util.control.TailCalls._

/**
  * Created by yuki.haneda on 2017/04/14.
  */

sealed trait List[+A]
case object Nil  extends List[Nothing]
case class Cons[+A](head : A, tail : List[A]) extends List[A]

object List {
  def apply[A] (as : A*) : List[A] = {
    def loop(k : List[A] => TailRec[List[A]], xs : Seq[A]) : TailRec[List[A]] =
      if (xs.isEmpty) k(Nil)
      else loop( x => tailcall(k(Cons(xs.head, x))), xs.tail)

    loop( x => done(x), as ).result
  }
}

