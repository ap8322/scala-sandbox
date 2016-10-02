trait Can {
  def sayReason() = println("Canです。")
}
trait Done extends Can {
  override def sayReason() = println("Doneです。")
}

class Person extends Can
class Animal extends Done

class SwimmyList[U >: Can <: Can](lists: U*) {
  val head = lists.head
  val tail = lists.tail

  def add(newElem: U): SwimmyList[U] =
    new SwimmyList(newElem :: lists.toList: _*)

  def sayAll() = lists.foreach(_.sayReason())
}

class test[U >: Can <: Can](test: U*) extends SwimmyList[U]
