package scaladog

import java.time.Instant

private[scaladog] case class ServiceCheckRequest(
    check: String,
    hostName: String,
    status: ServiceCheckStatus,
    timestamp: Instant,
    message: String,
    tags: Seq[String]
)

private[scaladog] object ServiceCheckRequest {
  implicit val writer: DDPickle.Writer[ServiceCheckRequest] = DDPickle.macroW
}
