package simpleHttpServer

import model.StatusCode
import model.StatusLine
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
      val status = getRequestStatusLine(r.readLine)

      getRequestSource(status.path) match {
        case None => notFound()
        case Some(file) => createResponse(file, StatusCode.OK)
      }
    }
  }

  private def printRequestHeader(in: BufferedReader) = {
    Iterator
      .continually(in.readLine)
      .takeWhile(line => line != null && !line.isEmpty)
      .foreach(println)
  }

  private def notFound()(implicit out: BufferedOutputStream) = {
      val BASE_DIR = "/Users/yuki/src/bitbucket.org/ap8322/scala-sandbox/src/main/scala/server/public"
      val NOT_FOUND_PAGE = "/404/index.html"

      createResponse(new File(BASE_DIR + NOT_FOUND_PAGE), StatusCode.NotFound)
  }

  private def createResponse(file: java.io.File, status: StatusCode)(implicit out: BufferedOutputStream) = {
      val CRLF = "\r\n".getBytes
      val content = Files.readAllBytes(file.toPath)

      out.write(getResponseStatusLine(status))
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

  private def getRequestSource(path: String): Option[java.io.File] = {
    // TODO configで指定できるようにする。
    val BASE_DIR = "/Users/yuki/src/bitbucket.org/ap8322/scala-sandbox/src/main/scala/server/public"

    val file = path match {
      case p if p.endsWith("/") => new File(BASE_DIR + path + "index.html")
      case _  => new File(BASE_DIR + path)
    }

    if(file.exists) {
      Some(file)
    } else {
      None
    }
  }

  // TODO responseHelper traitに処理を切り出す。
  private def getResponseStatusLine(status: StatusCode) = {
    val HTTP_VERSION = "HTTP/1.1"
    val SP = " "

    (HTTP_VERSION + SP + status.code + SP + status.message).getBytes
  }

  private def getContentType(file: File) = {
      val textType = List("html","css","plain")
      val imageType = List("jpeg","png","gif")

      val key = "Content-Type"
      val value = getFileExtension(file.getName) match {
        case conType if textType.exists(x => x == conType) => "text/" + conType + "; charset=UTF-8"
        case conType if imageType.exists(x => x == conType)=> "image/" + conType
        case conType if conType == "json" => "application/json"
      }

      (key + ": " + value).getBytes

  }

  // TODO 流石に雑すぎるのであとで直す
  private def getFileExtension(fileName: String) = {
    fileName.lastIndexOf('.') match {
      case i if i > 0 => fileName.substring(i+1)
      case _ => "plain"
    }
  }

  private def getContentLength(file: File) = {
      val length = file.length.toString
      val key = "Content-Length"

      (key + ": " + length).getBytes
  }


  private def getRequestStatusLine(line: String): StatusLine = {
    line.split("\\s+") match {
      case Array(s1, s2, s3) => StatusLine(s1, s2, s3)
    }
  }
}
