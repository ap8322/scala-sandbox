package simpleHttpServer.model

case class StatusLine(
  method: String,
  path: String,
  version: String
)
