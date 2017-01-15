package simpleHttpServer.model

import java.io._
import java.nio.charset.StandardCharsets
import java.util

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks.breakable
import scala.util.control.Breaks.break

import simpleHttpServer.utils.Conf.CRLF_STR
import simpleHttpServer.utils.Conf.BASE_DIR

// あとでmetadata,body,method の case classを作る
case class Request(status:StatusLine,resource: Option[Resource],metaData: Option[Seq[Map[String,String]]] = None,body: Option[Seq[Map[String,String]]] = None)

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

  private def parseRequest(in: InputStream): (String,Seq[Map[String,String]],Option[Seq[Map[String,String]]]) = {
    val request = new String(readRequest(in))

    val ar = request.split(CRLF_STR + CRLF_STR).toSeq

    val header = ar.head.split(CRLF_STR).toSeq

    // val body = parseBody(ar.last)
    val body = None


    val metaData = header.tail.map { data =>
      val a = data.split(": ")
      Map(a.head -> a(1))
    }
    (header.head,metaData,body)
  }

  private def parseBody(body: String): Option[Seq[Map[String,String]]] = {
    body match {
      case bo if bo eq "" => None
      case bo => {
        Some(
          bo.split("&").toSeq.map{b =>
            val v = b.split("=").toSeq
            Map(v.head -> v(1))
          }
        )
      }
    }
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
