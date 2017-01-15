package simpleHttpServer.model

import java.io.File

import simpleHttpServer.utils.Conf.NOT_FOUND_PAGE

case class Response(status: StatusCode, resource: Resource) {
  def getServerName = "Server: MyServer".getBytes
}

object Response {

  def apply(request: Request): Response = {
    request.resource match {
      case Some(resource) => new Response(StatusCode.OK,resource)
      case None => new Response(StatusCode.NotFound, Resource(getNotFoundPage))
    }
  }

  private def getNotFoundPage = {
    new File(NOT_FOUND_PAGE)
  }

}



