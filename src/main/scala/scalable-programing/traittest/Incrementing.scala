package traittest

trait Incrementing extends IntQueue {
  abstract override def put(x: Int) { super.put(x + 1)}
}