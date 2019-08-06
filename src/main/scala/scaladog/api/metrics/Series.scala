package scaladog.api.metrics

import java.time.Instant

import scaladog.DDPickle

case class Series(
    metric: String,
    points: Seq[Point],
    host: String = "",
    tags: Seq[String] = Seq.empty,
    @upickle.implicits.key("type") metricType: MetricType = MetricType.Gauge
)

object Series {
  implicit val writer: DDPickle.Writer[Series] = DDPickle.macroW
}

case class Point(timestamp: Instant, value: BigDecimal)

object Point {
    implicit val writer: DDPickle.Writer[Point] =
        DDPickle.writer[ujson.Arr].comap(p => ujson.Arr(p.timestamp.getEpochSecond, p.value.toString()))
}
