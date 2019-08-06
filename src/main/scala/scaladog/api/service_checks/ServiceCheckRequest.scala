package scaladog.api.service_checks

import java.time.Instant

import scaladog.DDPickle

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
