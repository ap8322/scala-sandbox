package simpleHttpServer.model

sealed abstract class StatusCode(val code: Int, val message: String)

object StatusCode {
  case object OK extends StatusCode(200,"OK")
  case object NotFound extends StatusCode(404,"Not Found")
}
