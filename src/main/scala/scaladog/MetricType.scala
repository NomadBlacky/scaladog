package scaladog

sealed abstract class MetricType(val name: String) {
  def asMetricType: MetricType = this
}

object MetricType {
  case object Gauge extends MetricType("gauge")
  case object Rate  extends MetricType("rate")
  case object Count extends MetricType("count")

  val all: Set[MetricType] = Set(Gauge, Rate, Count)

  def fromString(str: String): Option[MetricType] = all.find(_.name == str)

  implicit def readwriter: DDPickle.ReadWriter[MetricType] =
    DDPickle
      .readwriter[String]
      .bimap(
        _.name,
        s => fromString(s).getOrElse(throw new IllegalArgumentException(s"Invalid MetricType: $s"))
      )
}
