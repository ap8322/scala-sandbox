package simpleHttpServer.utils

import java.io.Closeable

class Using[T <: Closeable](source: T) {

  def foreach[U](f: T => U): U =
    try {
      f(source)
    } finally {
      source.close()
    }
}

object Using {
  def apply[T <: Closeable](source: T) = new Using(source)
}
