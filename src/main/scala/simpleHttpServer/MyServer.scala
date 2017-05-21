package simpleHttpServer

import java.io._
import java.net.{ServerSocket, Socket}

import simpleHttpServer.model.{Request, RequestMethod, Response}
import simpleHttpServer.utils.Conf.CRLF
import simpleHttpServer.utils.Using

class MyServer {
  def start(ss: ServerSocket) = {
    println("Waiting access...")

    while(true) {
      val socket = ss.accept

      new Thread() {
        override def run() = createResponse(socket)
      }.start()

    }
  }

  private def createResponse(socket: Socket) = {
    for {
      in <- Using(socket.getInputStream)
      out <- Using(new BufferedOutputStream(socket.getOutputStream))
    } {
      val request =  Request(in)
      val response = Response(request)

      // formで送られてきたデータ確認用。
      request.body.foreach{
        case (k,v) => println(k + " => " + v)
      }

      out.write(response.status.getResponseStatusLine)
      out.write(CRLF)

      if(request.status.method == RequestMethod.Get || request.status.method == RequestMethod.Head){
        out.write(response.resource.getContentType)
        out.write(CRLF)
        out.write(response.resource.getContentLength)
        out.write(CRLF)
      }

      out.write(response.getServerName)
      out.write(CRLF)
      out.write(CRLF)

      if(request.status.method == RequestMethod.Get){
        out.write(response.resource.content)
      }

      out.write(CRLF)
    }
  }
}
