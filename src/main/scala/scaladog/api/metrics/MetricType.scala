package scaladog.api.metrics

import enumeratum.EnumEntry
import enumeratum.EnumEntry.Snakecase
import scaladog.api.UPickleEnum

import scala.collection.immutable.IndexedSeq

sealed abstract class MetricType extends EnumEntry with Snakecase

object MetricType extends UPickleEnum[MetricType] {
  val values: IndexedSeq[MetricType] = findValues

  case object Gauge extends MetricType
  case object Rate  extends MetricType
  case object Count extends MetricType
}
