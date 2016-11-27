package simpleHttpServer

import java.io.{BufferedReader, BufferedWriter, InputStreamReader, OutputStreamWriter}
import java.net.{ServerSocket, Socket}
import java.nio.charset.StandardCharsets

class MyServer {
  def start(ss: ServerSocket) = {
    println("Waiting access...")

    Iterator
      .continually(ss.accept)
      .foreach { socket =>
        new Thread() {
          override def run() = {
            Thread.sleep(3000)
            request(socket)
          }
        }.start()
      }
  }

  private def request(socket: Socket) = {
    for {
      in <- Using(new BufferedReader(new InputStreamReader(socket.getInputStream, StandardCharsets.UTF_8)))
      out <- Using(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream, StandardCharsets.UTF_8)))
    } {
      printRequestHeader(in)
      out.write("<h1>hello world</h1>")
    }
  }

  private def printRequestHeader(in: BufferedReader) = {
    Iterator
      .continually(in.readLine)
      .takeWhile(line => line != null && !line.isEmpty)
      .foreach(println)
  }
}
