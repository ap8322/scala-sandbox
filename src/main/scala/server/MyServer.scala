package simpleHttpServer

import java.io.{BufferedReader, BufferedWriter, InputStreamReader, OutputStreamWriter}
import java.net.ServerSocket

class MyServer {
  def start(ss: ServerSocket) = {
    println("Waiting access...")

    for {
      socket <- Iterator.continually(ss.accept)
      in     <- Using(new BufferedReader(new InputStreamReader(socket.getInputStream, "UTF-8")))
      out    <- Using(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream, "UTF-8")))
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
      }
  }

  private def printRequestHeader(in: BufferedReader) = {
    Iterator
      .continually(in.readLine)
      .takeWhile(line => line != null && !line.isEmpty)
      .foreach(println)
  }
}
