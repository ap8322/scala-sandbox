package com.modoki

import scala.annotation.tailrec

abstract class AbstractMyList[+A] {
  def map[B](f: A => B): AbstractMyList[B]
  def foldLeft[B](z: B)(f: (B, A) => B): B
  def foldRight[B](z: B)(f: (A, B) => B): B
}

sealed trait MyList[+A] extends AbstractMyList[A] {
  self =>
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

  final def reverse: MyList[A] = foldLeft[MyList[A]](MyNil) { (acc, xs) => xs :: acc }
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
  // cont
  @tailrec
  final def foldRight[B](z: B)(f: (A, B) => B): B = {
    this match {
      case MyNil => z
      case mc: MyCons[A] => mc.init.foldRight(f(mc.last,z))(f)
    }
  }

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
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("head of empty MyList")
  def tail: MyList[Nothing] = throw new UnsupportedOperationException("tail of empty MyList")
}

final case class MyCons[B](hd: B, tl: MyList[B]) extends MyList[B] {
  def isEmpty: Boolean = false
  def head: B = hd
  def tail: MyList[B] = tl
}

object MyList {
  def apply[A](x: A*): MyList[A] =
    if (x.isEmpty) MyNil
    else MyCons(x.head, apply(x.tail: _*))
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

// option の実装
