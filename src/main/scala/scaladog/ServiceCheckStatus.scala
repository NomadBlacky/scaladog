package scaladog

sealed abstract class ServiceCheckStatus(val value: Int)

object ServiceCheckStatus {
  case object OK       extends ServiceCheckStatus(0)
  case object Warning  extends ServiceCheckStatus(1)
  case object Critical extends ServiceCheckStatus(2)
  case object Unknown  extends ServiceCheckStatus(3)

  implicit val writer: DDPickle.Writer[ServiceCheckStatus] =
    DDPickle.writer[Int].comap(_.value)
}
