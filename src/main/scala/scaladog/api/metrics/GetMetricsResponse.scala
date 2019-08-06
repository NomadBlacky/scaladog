package scaladog.api.metrics

import java.time.Instant

import scaladog.DDPickle

case class GetMetricsResponse(
    metrics: Seq[String],
    from: Instant,
    private val host: Option[String] = None
)

object GetMetricsResponse {
  implicit val reader: DDPickle.Reader[GetMetricsResponse] = DDPickle.macroR
}
