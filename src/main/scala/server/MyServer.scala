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
      out.write("HTTP/1.1 200 OK\n")
      out.write("content-Type: text/html; charset=UTF-8\n")
      out.write("Server: MyServer\n")
      out.write("\n")
      out.write("<!DOCTYPE html>")
      out.write("<html lang='ja'>")
      out.write("<head>")
      out.write("<meta charset='UTF-8'>")
      out.write("<title>test</title>")
      out.write("</head>")
      out.write("<body>")
      out.write("<h1>hello world</h1>")
      out.write("</body>")
      out.write("</html>")
      out.write("\n")
    }
  }

  private def printRequestHeader(in: BufferedReader) = {
    Iterator
      .continually(in.readLine)
      .takeWhile(line => line != null && !line.isEmpty)
      .foreach(println)
  }
}
