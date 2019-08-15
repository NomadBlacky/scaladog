package scaladog.api.events

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
}
