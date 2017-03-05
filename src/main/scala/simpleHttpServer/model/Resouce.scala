package simpleHttpServer.model

import java.nio.file.Files

/**
  * サーバーにあるファイル
  * @param content
  * @param contentType
  * @param length
  */
case class Resource(content: Array[Byte],contentType: ContentType,length: Long) {
  def getContentLength = ("Content-Length: " + length.toString).getBytes
  def getContentType = ("Content-Type: "+ contentType.message).getBytes
}

object Resource {
  def apply(file: java.io.File): Resource = {
    new Resource(
      getContent(file),
      ContentType(getFileExtension(file.getName)),
      file.length
    )
  }

  // TODO あとで書き直す
  private def getContent(file: java.io.File): Array[Byte] = {
    Files.readAllBytes(file.toPath)
  }

  // TODO 流石に雑すぎるのであとで直す
  private def getFileExtension(fileName: String): String = {
    fileName.lastIndexOf('.') match {
      case i if i > 0 => fileName.substring(i+1)
      case _ => "plain"
    }
  }
}
