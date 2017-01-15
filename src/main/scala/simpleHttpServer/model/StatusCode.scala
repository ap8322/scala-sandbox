package simpleHttpServer.model

sealed class StatusCode(val code: Int, val message: String, val version: String = "HTTP/1.1") {
  def getResponseStatusLine = {
    Seq(version,code,message).mkString(" ").getBytes
  }
}

object StatusCode {
  case object OK extends StatusCode(200,"OK")
  case object NotFound extends StatusCode(404,"Not Found")
}
