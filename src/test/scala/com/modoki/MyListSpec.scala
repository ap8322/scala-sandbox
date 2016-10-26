package com.modoki

import org.scalatest._

class MyListSpec extends FlatSpec {
  val target = MyList(1, 2, 3)

  "+" should "work like List" in {
    val expect = MyList(1,2,3,4)
    val actual = target + 4
    assert(expect === actual)
  }

  "map" should "work like List" in {
    val expect = MyList(2, 4, 6)
    val actual = target.map(x => x * 2)
    assert(expect === actual)
  }

  "foldLeft" should "work like List" in {
    val expect = "123"
    val actual = target.foldLeft("") { (acc, x) => acc + x }
    assert(expect === actual)
  }

  "foldRight" should "work like List" in {
    val expect = "321"
    val actual = target.foldRight("") { (x, acc) => acc + x }
    assert(expect === actual)
  }

  "last" should "work like List" in {
    val expect = 3
    val actual = target.last
    assert(expect === actual)
  }

  "init" should "work like List" in {
    val expect = MyList(1,2)
    val actual = target.init
    assert(expect === actual)
  }

  "filter" should "work like List" in {
    val expect = MyList(2)
    val actual = target.filter{ x => x%2 === 0}
    assert(expect === actual)
  }

  "reverse" should "work like List" in {
    val expect = MyList(3,2,1)
    val actual = target.reverse
    assert(expect === actual)
  }

  "flatMap" should "work like List" in {
    val expect = MyList(1, 1, 2, 2, 3, 3)
    val actual = target.flatMap { x => MyList(x, x) }
    assert(expect === actual)
  }

  "cps" should "hoge" in {
    def factorial(n: Int): Int = {
      @scala.annotation.tailrec
      def loop(i: Int, acc: Int = 1): Int = {
        if (i == 0) acc
        else loop(i - 1, acc * i)
      }
      loop(n, 1)
    }

    def fact2(n: Int): Int = {
      def loop(i: Int, acc: Int => Int): Int = {
        if (i == 0) acc(1)
        else loop(i - 1, a => acc(i * a))
      }
      loop(n, a => a)
    }

    assert(factorial(10) === (1 to 10).foldLeft(1)(_ * _))
    assert(fact2(10) === (1 to 10).foldLeft(1)(_ * _))

  }
}

