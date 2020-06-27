package scaladog.api.events

import enumeratum.EnumEntry.Lowercase
import enumeratum.{Enum, EnumEntry}
import scaladog.api.DDPickle

import scala.collection.immutable.IndexedSeq

sealed trait Priority extends EnumEntry with Lowercase

object Priority extends Enum[Priority] {
  case object Normal extends Priority
  case object Low extends Priority

  val values: IndexedSeq[Priority] = findValues

  implicit val readwriter: DDPickle.ReadWriter[Priority] =
    DDPickle.readwriter[String].bimap(_.entryName, withName)
}
