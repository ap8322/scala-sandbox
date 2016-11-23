package simpleHttpServer

import java.net.{ServerSocket, InetSocketAddress}

object Main {
  def main(args: Array[String]) {
    val serverSocket = new ServerSocket
    serverSocket.bind(new InetSocketAddress(8080))

    val server = new MyServer()
    server.start(serverSocket)
  }
}

