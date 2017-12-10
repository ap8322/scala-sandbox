package simpleHttpServer.model

import java.io._

import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks.{break, breakable}

import simpleHttpServer.utils.Conf.{BASE_DIR, CRLF_STR}

case class Request(
    status: StatusLine,
    metaData: Map[String, String] = Map.empty,
    body: Map[String, String] = Map.empty
)

object Request {
  type Metadata = Map[String, String]
  type RequestBody = Map[String, String]

  def apply(in: InputStream): Request = {
    // memo applyでかすぎると切り分けがちゃんとできていない｡
    // 高階関数で
    // 先にstatuslineを見て処理を分岐させる｡
    // おかしい奴は400
    //TODO  Stringに変換せずにやりたい｡
    val request = new String(readRequest(in))

    request.split(CRLF_STR + CRLF_STR) match {
      case Array(header, body) => {
        val (statusLine, metadata) = parseRequestHeader(header)
        val bodyParse = parseRequestBody(body)

        new Request(
          statusLine,
          metadata,
          bodyParse
        )

      }
      case Array(header) => {
        val (statusLine, metadata) = parseRequestHeader(header)

        new Request(
          statusLine,
          metadata,
          Map.empty[String, String]
        )
      }
    }
  }

  private def readRequest(in: InputStream): Array[Byte] = {
    // readerを使う
    var list = ListBuffer[Array[Byte]]()
    val read = new Array[Byte](1024)

    breakable {
      while (true) {
        val len = in.read(read)
        list += read
        if (len < read.length) {
          break
        }
      }
    }

    list.flatten.toArray
  }

  private def parseRequestHeader(header: String): (StatusLine, Metadata) = {
    val headers = header.split(CRLF_STR)

    val statusLine = StatusLine(headers.head)
    val metadatas = headers.tail

    val metaMap = metadatas.foldLeft(Map.empty[String, String]) {
      (acc, data) =>
        data.split(": ") match {
          case Array(key, value) => acc + (key -> value)
          case _ => acc
        }
    }

    (statusLine, metaMap)
  }

  private def parseRequestBody(body: String): RequestBody = {
    body.split("&").foldLeft(Map.empty[String, String]) { (acc, b) =>
      b.split("=") match {
        case Array(k, v) => acc + (k -> v)
        case _ => acc
      }
    }
  }

  private def getRequestResource(status: StatusLine): Option[Resource] = {
    val file = status.path match {
      case p if p.endsWith("/") => new File(BASE_DIR + p + "index.html")
      case p => new File(BASE_DIR + p)
    }

    if (file.exists) {
      Some(Resource(file))
    } else {
      None
    }
  }
}
