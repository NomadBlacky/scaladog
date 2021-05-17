package scaladog.api

import scala.annotation.nowarn

case class StatusResponse(status: String) {
  val isOk: Boolean = status.toLowerCase == "ok"
}

object StatusResponse {
  // https://github.com/com-lihaoyi/upickle/issues/345
  @nowarn implicit val reader: DDPickle.Reader[StatusResponse] = DDPickle.macroR
}
