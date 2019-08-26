package scaladog.api.events

import java.time.Instant

import scaladog.api.DDPickle

private[events] final case class PostEventRequest(
    title: String,
    text: String,
    dateHappened: Instant,
    priority: Priority,
    host: String,
    tags: Seq[String],
    alertType: AlertType,
    aggregationKey: String,
    sourceTypeName: String,
    relatedEventId: Long,
    deviceName: String
)

private[events] object PostEventRequest {
  implicit val writer: DDPickle.Writer[PostEventRequest] = DDPickle.macroW
}
