package scaladog.api.events

import scaladog.api.DDPickle

import scala.annotation.nowarn

case class GetEventResponse(event: Event)

object GetEventResponse {
  // https://github.com/com-lihaoyi/upickle/issues/345
  @nowarn implicit val reader: DDPickle.Reader[GetEventResponse] = DDPickle.macroR
}
