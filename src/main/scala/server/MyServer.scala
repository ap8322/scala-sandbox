package simpleHttpServer

import java.io._
import java.net.{ServerSocket, Socket}
import java.nio.charset.StandardCharsets

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
      w <- Using(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream, StandardCharsets.UTF_8)))
    } {
      implicit val out: BufferedWriter = w
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

  private def createHTML()(implicit out: BufferedWriter) = {
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

  private def notFound()(implicit out: BufferedOutputStream) = {
      out.write("HTTP/1.1 404 NotFound\n".getBytes)
      out.write("content-Type: text/html; charset=UTF-8\n")
      out.write("Server: MyServer\n")
      out.write("\n")
      out.write("<!DOCTYPE html>")
      out.write("<html lang='ja'>")
      out.write("<head>")
      out.write("<meta charset='UTF-8'>")
      out.write("<title>404</title>")
      out.write("</head>")
      out.write("<body>")
      out.write("<h1>Not Found...</h1>")
      out.write("</body>")
      out.write("</html>")
      out.write("\n")
  }

  private def createImageResponse()(implicit out: BufferedWriter) = {
      // imageデータを取ってくる
      val image = new File("/tmp/test.jpeg")
      val br = new BufferedReader(new InputStreamReader(new FileInputStream(image)))
      val buffer = new Array[Char](256)
      var len = 0

      out.write("HTTP/1.1 200 OK\n")
      out.write("content-Type: text/jpeg\n")
      out.write("content-Length: "+ 100000 +"\n")
      out.write("Server: MyServer\n")
      out.write("\n")

      while(len == -1){
        len = br.read(buffer)
        println(len)
        out.write(buffer,0,len)
      }

      out.write("\n")
  }

  case class StatusLine(method: String, path: String, version: String)

  private def getRequestLine(line: String): StatusLine = {
    line.split("\\s+") match {
      case Array(s1, s2, s3) => StatusLine(s1, s2, s3)
    }
  }

}
