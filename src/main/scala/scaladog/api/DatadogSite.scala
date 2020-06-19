package scaladog.api

import enumeratum.{Enum, EnumEntry}

import scala.collection.immutable.IndexedSeq

sealed abstract class DatadogSite(val domain: String) extends EnumEntry

object DatadogSite extends Enum[DatadogSite] {
  case object US extends DatadogSite("com")
  case object COM extends DatadogSite("com")
  case object EU extends DatadogSite("eu")

  val values: IndexedSeq[DatadogSite] = findValues
}
