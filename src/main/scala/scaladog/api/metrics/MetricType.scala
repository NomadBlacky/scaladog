package scaladog.api.metrics

import enumeratum.EnumEntry.Snakecase
import enumeratum.{Enum, EnumEntry}
import scaladog.api.DDPickle

import scala.collection.immutable.IndexedSeq

sealed abstract class MetricType extends EnumEntry with Snakecase

object MetricType extends Enum[MetricType] {
  val values: IndexedSeq[MetricType] = findValues

  case object Gauge extends MetricType
  case object Rate  extends MetricType
  case object Count extends MetricType

  implicit def readwriter: DDPickle.ReadWriter[MetricType] =
    DDPickle.readwriter[String].bimap(_.entryName, withName)
}
