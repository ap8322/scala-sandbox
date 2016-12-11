package simpleHttpServer

import java.io._
import java.net.{ServerSocket, Socket}
import java.nio.charset.StandardCharsets
import java.nio.file._

class MyServer {
  def start(ss: ServerSocket) = {
    println("Waiting access...")

    while(true) {
      val socket = ss.accept

      new Thread() {
        override def run() = routes(socket)
      }.start()
    }
  }

  private def routes(socket: Socket) = {
    for {
      r <- Using(new BufferedReader(new InputStreamReader(socket.getInputStream, StandardCharsets.UTF_8)))
      w <- Using(new BufferedOutputStream(socket.getOutputStream))
    } {
      implicit val out: BufferedOutputStream = w
      val reqLine = getRequestLine(r.readLine)

      reqLine.path match {
        case "/"      => createHTML()
        case "/image" => createImageResponse()
        case _        => notFound()
      }
    }
  }

  private def printRequestHeader(in: BufferedReader) = {
    Iterator
      .continually(in.readLine)
      .takeWhile(line => line != null && !line.isEmpty)
      .foreach(println)
  }

  private def createHTML()(implicit out: BufferedOutputStream) = {
      out.write("HTTP/1.1 200 OK\n".getBytes)
      out.write("content-Type: text/html; charset=UTF-8\n".getBytes)
      out.write("Server: MyServer\n".getBytes)
      out.write("\n".getBytes)
      out.write("<!DOCTYPE html>".getBytes)
      out.write("<html lang='ja'>".getBytes)
      out.write("<head>".getBytes)
      out.write("<meta charset='UTF-8'>".getBytes)
      out.write("<title>test</title>".getBytes)
      out.write("</head>".getBytes)
      out.write("<body>".getBytes)
      out.write("<h1>hello world</h1>".getBytes)
      out.write("</body>".getBytes)
      out.write("</html>".getBytes)
      out.write("\n".getBytes)
  }

  private def notFound()(implicit out: BufferedOutputStream) = {
      out.write("HTTP/1.1 404 NotFound\n".getBytes)
      out.write("content-Type: text/html; charset=UTF-8\n".getBytes)
      out.write("Server: MyServer\n".getBytes)
      out.write("\n".getBytes)
      out.write("<!DOCTYPE html>".getBytes)
      out.write("<html lang='ja'>".getBytes)
      out.write("<head>".getBytes)
      out.write("<meta charset='UTF-8'>".getBytes)
      out.write("<title>404</title>".getBytes)
      out.write("</head>".getBytes)
      out.write("<body>".getBytes)
      out.write("<h1>Not Found...</h1>".getBytes)
      out.write("</body>".getBytes)
      out.write("</html>".getBytes)
      out.write("\n".getBytes)
  }

  private def createImageResponse()(implicit out: BufferedOutputStream) = {
      val image = new File("/tmp/test.jpeg")
      val bytes = Files.readAllBytes(image.toPath)

      val len = file.length.toString.getBytes

      out.write("HTTP/1.1 200 OK\n".getBytes)
      out.write("Content-Type: image/jpeg\n".getBytes)
      out.write("Content-Length: ".getBytes)
      out.write(len)
      out.write("\n".getBytes)
      out.write("Server: MyServer\n".getBytes)
      out.write("\n".getBytes)

      out.write(bytes)

      out.write("\n".getBytes)
  }

  case class StatusLine(method: String, path: String, version: String)

  private def getRequestLine(line: String): StatusLine = {
    line.split("\\s+") match {
      case Array(s1, s2, s3) => StatusLine(s1, s2, s3)
    }
  }

}
