package scaladog.api

import enumeratum.values.{IntEnum, IntEnumEntry}
import enumeratum.{Enum, EnumEntry}

trait UPickleEnum[T <: EnumEntry] extends Enum[T] {
  implicit val readwriter: DDPickle.ReadWriter[T] =
    DDPickle.readwriter[String].bimap(_.entryName, withName)
}

trait IntUPickleEnum[T <: IntEnumEntry] extends IntEnum[T] {
  implicit val readwriter: DDPickle.ReadWriter[T] =
    DDPickle.readwriter[Int].bimap(_.value, withValue)
}
