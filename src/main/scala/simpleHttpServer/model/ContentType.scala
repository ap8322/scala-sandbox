package simpleHttpServer.model


sealed abstract class ContentType(val name: String,val message: String)

object ContentType{
  case object html extends ContentType("html","text/html; charset=UTF-8")
  case object css extends ContentType("css","text/css")
  case object plain extends ContentType("plain","text/plain")
  case object jpeg extends ContentType("jpeg","image/")

  val values = Seq(html,css,plain,jpeg)

  def apply(typeName: String): ContentType = {
    values.find(_.name == typeName).getOrElse(ContentType.plain)
  }
}
