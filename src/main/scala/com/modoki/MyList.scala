package com.modoki

sealed trait MyList[+A]{
  self =>
  def isEmpty: Boolean
  def head: A
  def tail: MyList[A]

  final def ::[B >: A](x: B): MyList[B] = MyCons(x, this)

  final def :::[B >: A](prefix: MyList[B]): MyList[B] = {
    if(prefix.isEmpty) this
    else prefix.head :: prefix.tail ::: this
  }

  def fold = ???

  val f = (x: Int) => x * 2

  final def length: Int =
    if(isEmpty) 0 else 1 + tail.length

  final def filter(f:A => Boolean): MyList[A] = {
    if(isEmpty) MyNil
    else{
      // パターンマッチ
      if(f(this.head)) this.head :: tail.filter(f)
      else tail.filter(f)
    }
  }

  final def map[U](f:A => U): MyList[U] =
    // 再帰でやる｡
    if(isEmpty) MyNil
    else f(head) :: tail.map(f)

  final def flatMap[U](f:A => MyList[U]): MyList[U] =
    if(isEmpty) MyNil
    else f(head) ::: tail.flatMap(f)

  final def foreach(f:A => Unit): Unit = {
    if(!isEmpty) {
      f(this.head)
      tail.foreach(f)
    }
  }

  final def withFilter(f:A => Boolean): WithFilter = new WithFilter(f)

  final class WithFilter(f:A => Boolean){
    def map[U](fn:A => U): MyList[U] = self filter f map fn
    def flatMap[U](fn:A => MyList[U]): MyList[U] = self filter f flatMap fn
    def foreach(fn:A => Unit): Unit = self filter f foreach fn
    def withFilter(fn:A => Boolean): WithFilter = new WithFilter(x => f(x) && fn(x))
  }
}

case object MyNil extends MyList[Nothing]{
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException("head of empty MyList")
  def tail: MyList[Nothing] = throw new UnsupportedOperationException("tail of empty MyList")
}

final case class MyCons[B](hd: B, tl: MyList[B]) extends MyList[B]{
  def isEmpty: Boolean = false
  def head: B = hd
  def tail: MyList[B] = tl
}

object MyList {
  def apply[A](x: A*): MyList[A] =
    if(x.isEmpty) MyNil
    else MyCons(x.head,MyList(x.tail: _*))
}
