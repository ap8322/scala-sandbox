package simpleHttpServer.model

case class StatusLine(
  method: RequestMethod,
  path: String,
  version: String
)

object StatusLine {
  def apply(line: String): StatusLine = {
    //
    line.split(" ") match {
      case Array(s1, s2, s3) => new StatusLine(RequestMethod(s1), s2, s3)
      // case arr => new Exception("") // リクエストが悪いのか｡サーバー側の問題なのか
    }
  }
}

sealed class RequestMethod(val value: String)

object RequestMethod{
  case object Get extends RequestMethod("GET")
  case object Head extends RequestMethod("HEAD")
  case object Post extends RequestMethod("POST")
  case object Put extends RequestMethod("PUT")
  case object Delete extends RequestMethod("DELETE")

  val values = Seq(Get,Head,Post,Put,Delete)

  def apply(typeName: String):RequestMethod = {
    values.find(_.value == typeName).getOrElse(throw new Exception)
  }
}
