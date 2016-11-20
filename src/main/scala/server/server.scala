import java.net._
import java.io._

import scala.io.Source

object server {
  def main(args: Array[String]) {
    val ss = new ServerSocket
    ss.bind(new InetSocketAddress(8080))

    println("Waiting access...")

    for {
      socket <- Iterator.continually(ss.accept)
      isr <- Using(new InputStreamReader(socket.getInputStream, "UTF-8"))
      in <- Using(new BufferedReader(isr))
      out <- Using(new PrintStream(socket.getOutputStream))
    } {
      Iterator.continually(in.readLine).takeWhile(line => line != null && !line.isEmpty).foreach(println)
      out.println("hello world")
    }
  }
}

class Using[T <: { def close() }](source: T) {

  def foreach[U](f: T => U): U =
    try {
      f(source)
    } finally {
      source.close()
    }
}

object Using {
  def apply[T <: { def close() }](source: T) = new Using(source)
}

