package _30

class Point(val x: Int, val y: Int) {
  override def hashCode = (x, y).##
  override def equals(other: Any): Boolean = other match {
    case o: Point => this.x == o.x && this.y == o.y
    case _ => false
  }
}
