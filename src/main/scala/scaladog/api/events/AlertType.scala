package scaladog.api.events

import enumeratum.EnumEntry
import enumeratum.EnumEntry.Snakecase
import scaladog.api.UPickleEnum

import scala.collection.immutable.IndexedSeq

sealed trait AlertType extends EnumEntry with Snakecase

object AlertType extends UPickleEnum[AlertType] {
  case object Info       extends AlertType
  case object Success    extends AlertType
  case object Warning    extends AlertType
  case object Error      extends AlertType
  case object UserUpdate extends AlertType

  val values: IndexedSeq[AlertType] = findValues
}
