package scaladog

import java.time.Instant

import DDPickle._

case class GetMetricsResponse(
    metrics: Seq[String],
    from: Instant,
    private val host: Option[String] = None
)

object GetMetricsResponse {
  implicit val reader: Reader[GetMetricsResponse] = macroR
}
