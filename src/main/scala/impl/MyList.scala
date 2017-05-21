package impl

import scala.util.control.TailCalls._

sealed abstract class MyList[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyList[A]

  val a = scala.collection.immutable.List(1,2,3)

  def ::[B >: A](x: B): MyList[B] = MyCons(x, this)

  def map[B](f:A => B): MyList[B] = {
      def loop(target: MyList[A],acc: MyList[B] = MyNil): MyList[B] = {
        target match {
          case MyNil => acc
          case MyCons(head,tail) => loop(tail,f(head) :: acc)
        }
      }

      loop(this).reverse
  }

  def reverse: MyList[A] = {
      def loop(target: MyList[A],acc: MyList[A] = MyNil): MyList[A] = {
        target match {
          case MyNil => acc
          case MyCons(head,tail) => loop(tail,head ::acc)
        }
      }

      loop(this)
  }

  def filter(f:A => Boolean): MyList[A] = {
      def loop(target: MyList[A],acc: MyList[A] = MyNil): MyList[A] = {
        target match {
          case MyNil => acc
          case MyCons(head,tail) if f(head) => loop(tail,head :: acc)
          case MyCons(_,tail) => loop(tail,acc)
        }
      }

      loop(this).reverse
  }

  def :::[B >: A](l: MyList[B]): MyList[B] = {
    def loop(a1: MyList[B], a2: MyList[B]): MyList[B] = {
      a1 match {
        case MyNil => a2
        case MyCons(h,t) => MyCons(h, loop(t, a2))
      }
    }

    loop(l,this)
  }


  def flatMap[B](f:A => MyList[B]): MyList[B] = {
      def loop(target: MyList[A],acc: MyList[B] = MyNil): MyList[B] = {
        target match {
          case MyNil => acc
          case MyCons(head,tail) => loop(tail,f(head) ::: acc)
        }
      }

      loop(this).reverse
  }

  def foreach(f: A => Unit): Unit = {
    this match {
      case MyNil => ()
      case MyCons(h, t) => f(h); t.foreach(f)
    }
  }

  def foldLeft[B](z: B)(f: (B, A) => B): B = {
      def loop(l: MyList[A], z: B)(f: (B, A) => B): B = l match {
        case MyNil => z
        case MyCons(h,t) => loop(t, f(z,h))(f)
      }

      loop(this,z)(f)
  }

  def foldRight[B](z: B)(f: (A, B) => B): B = {
    def loop(xs: MyList[A], acc: B => B): B => B = {
      xs match {
        case MyNil => acc
        case MyCons(h,t) => loop(t,x => acc(f(h,x)))
      }
    }

    loop(this,x => x)(z)
  }

  // List(1,2,3,4,5).foldRight(0)((x,acc) => x + acc)
  // (1,(2,3,4,5)) => loop((2,3,4,5), x => f(1,x))
  // (2,(3,4,5) => loop((3,4,5), x => f(1,f(2,x)))
  // (3,(4,5) => loop((4,5), x => f(1,f(2,f(3,f(4,f(5,x)))))

  final def map2[B](f: A => B): MyList[B] = foldLeft[MyList[B]](MyNil) { (acc, xs) => f(xs) :: acc }.reverse

  final def map3[B](f: A => B): MyList[B] = foldRight(MyList[B]()){ (xs, acc) => f(xs) :: acc }

  final def flatMap2[B](f: A => MyList[B]): MyList[B] = foldLeft[MyList[B]](MyNil) { (acc, xs) => acc ::: f(xs) }.reverse

  final def filter2(f: A => Boolean): MyList[A] = foldLeft[MyList[A]](MyNil) { (acc, xs) => if (f(xs)) xs :: acc else acc }.reverse

  final def foldRight2[B](z: B)(f: (A,B) => B): B = foldLeft((x:B) => x){ (acc, xs) => x => acc(f(xs,x))}(z)

  // final def :::[B >: A](that: MyList[B]): MyList[B] = foldLeft(that) { (acc, xs) => acc + xs }

  // self =>
  // final def withFilter(f: A => Boolean): WithFilter = new WithFilter(f)
  // final class WithFilter (f: A => Boolean) {
  //   def map[U](fn: A => U): MyList[U] = self filter f map fn
  //   def flatMap[U](fn: A => MyList[U]): MyList[U] = self filter f flatMap fn
  //   def foreach(fn: A => Unit): Unit = self filter f foreach fn
  //   def withFilter(fn: A => Boolean): WithFilter = new WithFilter(x => f(x) && fn(x))
  // }
  // final def +[B >: A](x: B): MyList[B] = foldRight[MyList[B]](MyCons(x,MyNil)){ (xs,acc) => xs :: acc}

}

case object MyNil extends MyList[Nothing] {
  override def isEmpty: Boolean = true
  override def head: Nothing = throw new NoSuchElementException("head of empty MyList")
  override def tail: MyList[Nothing] = throw new UnsupportedOperationException("tail of empty MyList")
}

final case class MyCons[B](hd: B, tl: MyList[B]) extends MyList[B] {
  override def isEmpty: Boolean = false
  override def head: B = hd
  override def tail: MyList[B] = tl
}

object MyList {
  def apply[A] (as : A*) : MyList[A] = {
    //  if (as.isEmpty) MyNil
    //  else MyCons(as.head,apply(as.tail: _*))

    //
    def loop(k : MyList[A] => MyList[A], xs : Seq[A]) : MyList[A] =
      if (xs.isEmpty) k(MyNil)
      else loop( x => k(MyCons(xs.head, x)), xs.tail)

    loop(x => x,as)

    //    def loop(k : MyList[A] => TailRec[MyList[A]], xs : Seq[A]) : TailRec[MyList[A]] =
    //      if (xs.isEmpty) k(MyNil)
    //      else loop( x => tailcall(k(MyCons(xs.head, x))), xs.tail)
    //
    //    loop(MyNil => done(MyNil),as).result
  }
}

