package simpleHttpServer

import java.io.{BufferedReader, BufferedWriter, InputStreamReader, OutputStreamWriter}
import java.net.ServerSocket

class MyServer {
  def start(ss: ServerSocket) = {
    println("Waiting access...")

    for {
      socket <- Iterator.continually(ss.accept)
      in <- Using(new BufferedReader(new InputStreamReader(socket.getInputStream, "UTF-8")))
      out <- Using(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream, "UTF-8")))
      } {
        printRequestHeader(in)
        out.write("hello world")
      }
  }

  private def printRequestHeader(in: BufferedReader) = {
    Iterator
      .continually(in.readLine)
      .takeWhile(line => line != null && !line.isEmpty)
      .foreach(println)
  }
}
