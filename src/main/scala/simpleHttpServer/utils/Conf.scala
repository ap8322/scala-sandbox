package simpleHttpServer.utils

object Conf {
  val BASE_DIR =
    "/Users/yuki.haneda/src/bitbucket.org/ap8322/scala-sandbox/src/main/scala/simpleHttpServer/public"
  val NOT_FOUND_PAGE = BASE_DIR + "/404/index.html"

  val CR = "\r"
  val LF = "\n"
  val CRLF_STR = CR + LF
  val CRLF = CRLF_STR.getBytes
}
