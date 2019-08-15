package scaladog.api.events

import java.time.Instant

import requests.Requester
import scaladog.api.{APIClient, APIClientFactory, DatadogSite}

trait EventsAPIClient extends APIClient {
  def postEvent(
      title: String,
      text: String,
      dateHappened: Instant = Instant.now(),
      priority: Priority = Priority.Normal,
      host: String = "",
      tags: Seq[String] = Seq.empty,
      alertType: AlertType = AlertType.Info,
      aggregationKey: String = "",
      sourceTypeName: String = "",
      relatedEventId: Long = 0,
      deviceName: String = ""
  ): PostEventResponse

  def getEvent(id: Long): Event
}

object EventsAPIClient extends APIClientFactory[EventsAPIClient] {
  def apply(apiKey: String, appKey: String, site: DatadogSite): EventsAPIClient =
    new EventsAPIClientImpl(apiKey, appKey, site)
}

private[events] class EventsAPIClientImpl(
    protected val apiKey: String,
    protected val appKey: String,
    val site: DatadogSite,
    protected val _requester: Option[Requester] = None
) extends EventsAPIClient {
  override def postEvent(
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
  ): PostEventResponse = {
    val request = PostEventRequest(
      title,
      text,
      dateHappened,
      priority,
      host,
      tags,
      alertType,
      aggregationKey,
      sourceTypeName,
      relatedEventId,
      deviceName
    )
    httpPost[PostEventRequest, PostEventResponse]("/events", request)
  }

  def getEvent(id: Long): Event = httpGet[GetEventResponse](s"/events/$id").event
}
