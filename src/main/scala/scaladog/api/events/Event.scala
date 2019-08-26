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
    deviceName: Option[String] = None,
    source: Option[String] = None,
    tags: Seq[String] = Seq.empty,
    comments: Seq[CommentInfo] = Seq.empty,
    children: Seq[ChildEvent] = Seq.empty
)

object Event {
  implicit val reader: DDPickle.Reader[Event] = DDPickle.macroR
}

final case class ChildEvent(id: Long, alertType: AlertType, dateHappened: Instant)

object ChildEvent {
  implicit val reader: DDPickle.Reader[ChildEvent] = DDPickle.macroR
}

final case class CommentInfo(id: Long, alertType: AlertType, dateHappened: Instant)

object CommentInfo {
  implicit val reader: DDPickle.Reader[CommentInfo] = DDPickle.macroR
}
