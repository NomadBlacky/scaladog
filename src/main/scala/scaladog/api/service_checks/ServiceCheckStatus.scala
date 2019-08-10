package scaladog.api.service_checks

import enumeratum.values.{IntEnum, IntEnumEntry}
import scaladog.api.DDPickle

import scala.collection.immutable.IndexedSeq

sealed abstract class ServiceCheckStatus(val value: Int) extends IntEnumEntry

object ServiceCheckStatus extends IntEnum[ServiceCheckStatus] {
  case object OK       extends ServiceCheckStatus(0)
  case object Warning  extends ServiceCheckStatus(1)
  case object Critical extends ServiceCheckStatus(2)
  case object Unknown  extends ServiceCheckStatus(3)

  val values: IndexedSeq[ServiceCheckStatus] = findValues

  implicit val writer: DDPickle.Writer[ServiceCheckStatus] =
    DDPickle.writer[Int].comap(_.value)
}
