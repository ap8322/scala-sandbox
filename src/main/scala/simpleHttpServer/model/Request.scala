package simpleHttpServer.model

import java.io._
import java.nio.charset.StandardCharsets
import java.util

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks.breakable
import scala.util.control.Breaks.break

import simpleHttpServer.utils.Conf.CRLF_STR
import simpleHttpServer.utils.Conf.BASE_DIR

case class Request(status:StatusLine,resource: Option[Resource],metaData: Option[Seq[Map[String,String]]] = None,body: Option[Array[Byte]] = None)

object Request {
  def apply(in: InputStream): Request = {
    val header = parseRequest(in)
    val status = getRequestStatusLine(header._1)

    status.method match {
      case "GET" | "HEAD" => new Request(status, getRequestResource(status))
      case "POST" => ???
      case "PUT" => ???
    }
  }

  private def getRequestStatusLine(line: String): StatusLine = {
    line.split("\\s+") match {
      case Array(s1, s2, s3) => StatusLine(s1, s2, s3)
    }
  }

  private def parseRequest(in: InputStream): (String,Seq[Map[String,String]]) = {
    val request = new String(readRequest(in))

    val ar = request.split(CRLF_STR + CRLF_STR).toSeq

    val header = ar.head.split(CRLF_STR).toSeq
    //val body = ar.last.getBytes

    val metaData = header.tail.map { data =>
      val a = data.split(": ")
      Map(a.head -> a(1))
    }
    (header.head,metaData)
  }

  private def readRequest(in: InputStream): Array[Byte] = {
    var list = ListBuffer[Array[Byte]]()
    val read = new Array[Byte](1024)

    breakable {
      while(true) {
        val len = in.read(read)
        list += read
        if(len < read.length) {
          break
        }
      }
    }

    list.flatten.toArray
  }

  private def readBody(in: InputStream) = {
    println("reader")
    val bytes = new Array[Byte](2)

    println("read")
    println(in.read(bytes))
    print("read!")
  }

  private def getRequestResource(status: StatusLine): Option[Resource] = {
    val file = status.path match {
      case p if p.endsWith("/") => new File(BASE_DIR + p + "index.html")
      case p  => new File(BASE_DIR + p)
    }

    if(file.exists) {
      Some(Resource(file))
    } else {
      None
    }
  }
}
