package scaladog

case class StatusResponse(status: String) {
  val isOk: Boolean = status.toLowerCase == "ok"
}
