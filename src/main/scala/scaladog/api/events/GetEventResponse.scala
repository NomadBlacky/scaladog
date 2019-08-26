package scaladog.api.events

import scaladog.api.DDPickle

case class GetEventResponse(event: Event)

object GetEventResponse {
  implicit val reader: DDPickle.Reader[GetEventResponse] = DDPickle.macroR
}
