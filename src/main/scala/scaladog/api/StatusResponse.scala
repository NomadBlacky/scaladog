package scaladog.api

case class StatusResponse(status: String) {
  val isOk: Boolean = status.toLowerCase == "ok"
}

object StatusResponse {
  implicit val reader: DDPickle.Reader[StatusResponse] = DDPickle.macroR
}
