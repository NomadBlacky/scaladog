package scaladog.api.events

import scaladog.api.DDPickle

private[events] case class QueryEventsResponse(events: Seq[Event])

private[events] object QueryEventsResponse {
  implicit val reader: DDPickle.Reader[QueryEventsResponse] = DDPickle.macroR
}
