package scaladog.api.events

import java.time.Instant

import scaladog.ScaladogUnitTest

class EventsAPIClientImplTest extends ScaladogUnitTest {

  it("postEvent") {
    val requester = genTestRequester(
      url = "https://api.datadoghq.com/api/v1/events",
      200,
      """{
        |  "status": "ok",
        |  "event": {
        |    "id": 1234567890123456789,
        |    "title": "TEST EVENT",
        |    "text": "This is a test event.",
        |    "date_happened": 1565424331,
        |    "handle": null,
        |    "priority": "normal",
        |    "related_event_id": null,
        |    "tags": [
        |      "project:scaladog"
        |    ],
        |    "url": "https://app.datadoghq.com/event/event?id=1234567890123456789"
        |  }
        |}
      """.stripMargin.trim
    )
    val client = new EventsAPIClientImpl(apiKey, appKey, site, Some(requester))

    val actual = client.postEvent("TITLE", "This is a test event.")
    val expect =
      PostEventResponse("ok", 1234567890123456789L, "https://app.datadoghq.com/event/event?id=1234567890123456789")

    assert(actual == expect)
  }

  it("getEvent") {
    val requester = genTestRequester(
      url = "https://api.datadoghq.com/api/v1/events",
      statusCode = 200,
      body = """{
          |  "event": {
          |    "date_happened": 1565429042,
          |    "alert_type": "user_update",
          |    "resource": "/api/v1/events/5052730002120180683",
          |    "title": "TEST EVENT",
          |    "url": "/event/event?id=5052730002120180683",
          |    "text": "# This is a test event.\n\n+ hoge\n+ foo",
          |    "tags": [
          |      "project:scaladog"
          |    ],
          |    "device_name": null,
          |    "priority": "normal",
          |    "host": null,
          |    "id": 5052730002120180683
          |  }
          |}
        """.stripMargin.trim
    )

    val client = new EventsAPIClientImpl(apiKey, appKey, site, Some(requester))

    val actual = client.getEvent(0L)
    val expect = Event(
      id = 5052730002120180683L,
      text = "# This is a test event.\n\n+ hoge\n+ foo",
      dateHappened = Instant.ofEpochSecond(1565429042L),
      title = Some("TEST EVENT"),
      alertType = AlertType.UserUpdate,
      priority = Priority.Normal,
      host = None,
      tags = Some(Seq("project:scaladog")),
      aggregationKey = None,
      sourceTypeName = None,
      relatedEventId = None,
      deviceName = None
    )

    assert(actual == expect)
  }
}
