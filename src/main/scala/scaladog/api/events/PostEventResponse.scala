package scaladog.api.events

import scaladog.api.DDPickle

case class PostEventResponse(status: String, id: Long, url: String) {
  def isOk: Boolean = status == "ok"
}

object PostEventResponse {
  implicit val reader: DDPickle.Reader[PostEventResponse] =
    DDPickle.reader[PostEventResponseDTO].map { dto =>
      PostEventResponse(dto.status, dto.event.id, dto.event.url)
    }
}

// workaround: https://github.com/lihaoyi/upickle/issues/278
private[events] final case class PostEventResponseDTO(status: String, event: EventInPostEventResponse)

private[events] object PostEventResponseDTO {
  implicit val reader: DDPickle.Reader[PostEventResponseDTO] = DDPickle.macroR
}

private[events] final case class EventInPostEventResponse(id: Long, url: String)

private[events] object EventInPostEventResponse {
  implicit val reader: DDPickle.Reader[EventInPostEventResponse] = DDPickle.macroR
}
