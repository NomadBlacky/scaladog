package scaladog.api.events

import java.time.Instant

import scaladog.api.DDPickle

final case class Event(
    id: Long,
    text: String,
    dateHappened: Instant,
    priority: Priority,
    alertType: AlertType,
    title: Option[String] = None,
    host: Option[String] = None,
    tags: Seq[String] = Seq.empty,
    aggregationKey: Option[String] = None,
    sourceTypeName: Option[String] = None,
    relatedEventId: Option[Long] = None,
    deviceName: Option[String] = None
)

object Event {
  implicit val reader: DDPickle.Reader[Event] = DDPickle.macroR
}
