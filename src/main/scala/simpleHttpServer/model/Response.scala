package simpleHttpServer.model

import java.io.File

import simpleHttpServer.model.RequestMethod._
import simpleHttpServer.utils.Conf.NOT_FOUND_PAGE
import simpleHttpServer.utils.Conf.BASE_DIR

case class Response(
    status: StatusCode,
    resource: Resource
) {
  def getServerName = "Server: MyServer".getBytes
}

object Response {

  // impliciit parametar
  def apply(request: Request): Response = {
    request.status.method match {
      case Get | Head => {
        getRequestResource(request.status) match {
          case Some(resource) => new Response(StatusCode.OK, resource)
          case None =>
            new Response(StatusCode.NotFound,
                         Resource(new File(NOT_FOUND_PAGE)))
        }
      }
      case Post =>
        new Response(StatusCode.OK, Resource(new File(NOT_FOUND_PAGE)))
      case Put => ???
      case Delete => ???
    }
  }

  private def getRequestResource(status: StatusLine): Option[Resource] = {
    // TODO ディレクトリ名で終わってもindex.htmlでもやる｡ 再帰的にやるといい｡
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
