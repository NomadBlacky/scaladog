package scaladog.api.events

import scaladog.api.DDPickle

import scala.annotation.nowarn

private[events] case class QueryEventsResponse(events: Seq[Event])

private[events] object QueryEventsResponse {
  // https://github.com/com-lihaoyi/upickle/issues/345
  @nowarn implicit val reader: DDPickle.Reader[QueryEventsResponse] = DDPickle.macroR
}
