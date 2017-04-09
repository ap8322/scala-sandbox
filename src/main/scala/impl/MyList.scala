package impl

import scala.annotation.tailrec
import scala.AnyRef
import scala.collection.{AbstractSeq, LinearSeq}
import scala.collection.immutable.List
import scala.util.control.TailCalls

abstract class AbstractMyList[+A] {
  def map[B](f: A => B): AbstractMyList[B]
  def foldLeft[B](z: B)(f: (B, A) => B): B
  def foldRight[B](z: B)(f: (A, B) => B): B
}

sealed trait MyList[+A] {
  self =>
  def length: Int
  def isEmpty: Boolean
  def head: A
  def tail: MyList[A]

  final def ::[B >: A](x: B): MyList[B] = MyCons(x, this)
  final def :::[B >: A](that: MyList[B]): MyList[B] = foldLeft(that) { (acc, xs) => acc + xs }
  final def +[B >: A](x: B): MyList[B] = foldRight[MyList[B]](MyCons(x,MyNil)){ (xs,acc) => xs :: acc}
//  final def +[B >: A](x: B): MyList[B] = (x :: this.reverse).reverse

  final def init: MyList[A] = {
    @tailrec
    def createInit(xs: MyList[A], acc: MyList[A] = MyNil): MyList[A] = xs match {
      case MyNil => MyNil
      case MyCons(h, MyNil) => acc.reverse
      case MyCons(h, t) => createInit(t, h :: acc)
    }

    createInit(this)
  }
  final def last: A = this.reverse.head

  final def map2[B](f:A => B): MyList[B] = {
    // target がどんどん短くなり、accがどんどん形成されていく
    // targetが空MyNilになった時、accを返す。
    def loop(target: MyList[A],acc: MyList[B] = MyNil): MyList[B] = {
      target match {
        case MyNil => acc
        case MyCons(head,tail) => loop(tail,f(head) :: acc)
      }
    }

    loop(this)
  }

  // final def reverse: MyList[A] = foldLeft[MyList[A]](MyNil) { (acc, xs) => xs :: acc }
  final def reverse: MyList[A] = {
    def loop(xs: MyList[A],acc: MyList[A] = MyNil):MyList[A] = { xs match {
      case MyNil => acc
      case MyCons(h,t) => MyCons(h,acc)

    }

    }

  }
  final def filter(f: A => Boolean): MyList[A] = foldLeft[MyList[A]](MyNil) { (acc, xs) => if (f(xs)) xs :: acc else acc }.reverse

   @tailrec
   final def foldLeft[B](z: B)(f: (B, A) => B): B = {
     this match {
       case MyNil => z
       case MyCons(h, t) => t.foldLeft(f(z, h))(f)
     }
   }



  // 処理の最初と最後を考える｡
  // Trampoline recursion
  // cps continuation passing style
  // Free モナド
  // cont
  //@tailrec
  //  final def foldRight[B](z: B)(f: (A, B) => B): B = {
  //    def loop(xs: MyList[A], acc: B => B = x => x): B => B = xs match {
  //      case MyNil => acc
  //      case MyCons(h,t) => loop(t,x => acc(f(h,x)))
  //    }
  //
  // List(1,2,3,4,5).foldRight(0)((x,acc) => x + acc)
  // (1,(2,3,4,5)) => loop((2,3,4,5), x => f(1,x))
  // (2,(3,4,5) => loop((3,4,5), x => f(1,f(2,x)))
  // (3,(4,5) => loop((4,5), x => f(1,f(2,f(3,f(4,f(5,x)))))
  //
  //
  //    loop(this)(z)
  //  }

  final def foldr[B](z: B)(f: (A, B) => B): B = {
    def loop(as: MyList[A],z:B)(f: (A,B) => B): B = {
      as match {
        case MyNil => z
        case MyCons(x,xs) => f(x,loop(xs,z)(f))
      }
    }

    loop(this,z)(f)
  }

  final def foldRight[B](z: B)(f: (A,B) => B): B = foldLeft((x:B) => x){ (acc, xs) => x => acc(f(xs,x))}(z)

  final def map[B](f: A => B): MyList[B] = foldLeft[MyList[B]](MyNil) { (acc, xs) => f(xs) :: acc }.reverse
  final def flatMap[B](f: A => MyList[B]): MyList[B] = foldLeft[MyList[B]](MyNil) { (acc, xs) => acc ::: f(xs) }
  final def withFilter(f: A => Boolean): WithFilter = new WithFilter(f)

  final class WithFilter (f: A => Boolean) {
    def map[U](fn: A => U): MyList[U] = self filter f map fn
    def flatMap[U](fn: A => MyList[U]): MyList[U] = self filter f flatMap fn
    def foreach(fn: A => Unit): Unit = self filter f foreach fn
    def withFilter(fn: A => Boolean): WithFilter = new WithFilter(x => f(x) && fn(x))
  }

  final def foreach(f: A => Unit): Unit = {
    this match {
      case MyNil => ()
      case MyCons(h, t) => f(h); t.foreach(f)
    }
  }

  //  final def length: Int = if (isEmpty) 0 else 1 + tail.length
  //  final def filter(f: A => Boolean): MyList[A] = {
  //    def createFilter(xs: MyList[A], acc: MyList[A] = MyNil): MyList[A] = xs match {
  //      case MyNil => acc
  //      case MyCons(h, t) => if (f(h)) loop(t, h :: acc) else loop(t, acc)
  //    }
  //    createFilter(this)
  //  }
  //  final def :::[B >: A](prefix: MyList[B]): MyList[B] = {
  //    def createCons(xs: MyList[B], acc: MyList[B]): MyList[B] = xs match {
  //      case MyNil => acc
  //      case MyCons(h, t) => loop(t, acc + h)
  //    }
  //    createCons(this, prefix)
  //  }
  //  final def map[U](f: A => U): MyList[U] = {
  //    def createMap(xs: MyList[A], acc: MyList[U] = MyNil): MyList[U] = xs match {
  //      case MyNil => acc
  //      case MyCons(h, t) => loop(t, f(h) :: acc)
  //    }
  //    createMap(this)
  //  }
  //  final def flatMap[U](f: A => MyList[U]): MyList[U] = {
  //    def createFlatMap(xs: MyList[A], acc: MyList[U] = MyNil): MyList[U] = xs match {
  //      case MyNil => acc
  //      case MyCons(h, t) => loop(t, f(h) ::: acc)
  //    }
  //    createFlatMap(this)
  //  }
  //  final def map0[U](f: A => U): MyList[U] =
  //    if (isEmpty) MyNil else f(head) :: tail.map(f)
  //
  //  final def flatMap0[U](f: A => MyList[U]): MyList[U] =
  //    if (isEmpty) MyNil else f(head) ::: tail.flatMap(f)

}

case object MyNil extends MyList[Nothing] {
  override def length = 0
  override def isEmpty: Boolean = true
  override def head: Nothing = throw new NoSuchElementException("head of empty MyList")
  override def tail: MyList[Nothing] = throw new UnsupportedOperationException("tail of empty MyList")
}

final case class MyCons[B](hd: B, tl: MyList[B]) extends MyList[B] {
  override def length: Int = 1 + tl.length
  override def isEmpty: Boolean = false
  override def head: B = hd
  override def tail: MyList[B] = tl

}

object MyList {
//  def apply[A](x: A*): MyList[A] =
//    if (x.isEmpty) MyNil
//    else MyCons(x.head, apply(x.tail: _*))
  def apply[A](as : A*) : MyList[A] = {
    def loop(k : MyList[A] => MyList[A], xs : Seq[A]) : MyList[A] =
      if (xs.isEmpty) k(MyNil)
      else loop(x => k(MyCons(xs.head, x)), xs.tail)

    loop(x => x, as)
  }
}

case class Cont[A, B](run: (A => B) => B) {
  def map[C](f: A => C): Cont[C, B] = {
    Cont {
      k => run(a => k(f(a)))
    }
  }

  def flatMap[C](f: A => Cont[C, B]): Cont[C, B] = {
    ???
  }

}
