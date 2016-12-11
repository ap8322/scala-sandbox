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

      createResponse(reqLine.path)

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

  private def createResponseHeader(res: ResponseInfo)(implicit out: BufferedOutputStream) = {}

  private def createResponse(path: String)(implicit out: BufferedOutputStream) = {
      val base_dir = "/tmp"
      val CRLF = Array(13,10)

      val file = new File(base_dir + path)
      val content = Files.readAllBytes(file.toPath)

      out.write("HTTP/1.1 200 OK".getBytes)
      out.write(CRLF)
      out.write(getContentType(file))
      out.write(CRLF)
      out.write(getContentLength(file))
      out.write(CRLF)
      out.write("Server: MyServer".getBytes)
      out.write(CRLF)
      out.write(CRLF)
      out.write(content)
      out.write(CRLF)
  }

  private def getFileExtension(fileName: Strng) = {
    fileName.lastIndexOf('.') match {
      case i if i > 0 => fileName.substring(i+1)
      case _ => "plain"
    }
  }

  private def getContentType(file: File) = {
      val ex = getFileExtension(file.getName)
      val textType = List("html","css","plain")
      val imageType = List("jpeg","png","gif")

      val key = "Content-Type"
      val value = ex match {
        case ct if textType.exists(x => x = ct) => "text/" + ct + "; charset=UTF-8"
        case ct if imageType.exists(x => x = ct) => "image/" + ct
      }

      (key + ": " + value).getBytes

  }

  private def getContentLength(file: File) = {
      val len = file.length.toString.getBytes
      val key = "Content-Length"

      (key + ": " len).getBytes
  }

  case class StatusLine(method: String, path: String, version: String)

  private def getRequestLine(line: String): StatusLine = {
    line.split("\\s+") match {
      case Array(s1, s2, s3) => StatusLine(s1, s2, s3)
    }
  }

}
