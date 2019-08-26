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
          |    "device_name": "My Device",
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
      deviceName = Some("My Device"),
      source = None,
      tags = Seq("project:scaladog"),
      comments = Seq.empty,
      children = Seq.empty
    )

    assert(actual == expect)
  }

  it("query") {
    val requester = genTestRequester(
      url = "https://api.datadoghq.com/api/v1/events",
      statusCode = 200,
      body = """{
               |    "events": [
               |        {
               |            "date_happened": 1566742405,
               |            "alert_type": "info",
               |            "is_aggregate": true,
               |            "title": "a test event",
               |            "url": "/event/event?id=5074764735231597726",
               |            "text": "a test text.",
               |            "tags": [
               |                "project:scaladog"
               |            ],
               |            "comments": [
               |                {
               |                    "date_happened": 1566742541,
               |                    "alert_type": "user_update",
               |                    "id": 5074766851559694338
               |                }
               |            ],
               |            "children": [
               |                {
               |                    "date_happened": 1566742405,
               |                    "alert_type": "info",
               |                    "id": "5074764569206621403"
               |                },
               |                {
               |                    "date_happened": 1566742415,
               |                    "alert_type": "info",
               |                    "id": "5074764741922631056"
               |                }
               |            ],
               |            "priority": "normal",
               |            "source": "datadog",
               |            "host": null,
               |            "resource": "/api/v1/events/5074764735231597726",
               |            "device_name": "my device",
               |            "id": 5074764735231597726
               |        },
               |        {
               |            "date_happened": 1566741933,
               |            "alert_type": "info",
               |            "is_aggregate": false,
               |            "title": "a test event 2",
               |            "url": "/event/event?id=5074756651795870425",
               |            "text": "a test text 2.",
               |            "tags": [
               |                "tag_key:tag_value"
               |            ],
               |            "comments": [],
               |            "device_name": null,
               |            "priority": "low",
               |            "source": "My Apps",
               |            "host": null,
               |            "resource": "/api/v1/events/5074756651795870425",
               |            "id": 5074756651795870425
               |        }
               |    ]
               |}
             """.stripMargin.trim
    )

    val client = new EventsAPIClientImpl(apiKey, appKey, site, Some(requester))

    val actual = client.query(Instant.now(), Instant.now())
    val expect = Seq(
      Event(
        id = 5074764735231597726L,
        text = "a test text.",
        dateHappened = Instant.ofEpochSecond(1566742405L),
        title = Some("a test event"),
        alertType = AlertType.Info,
        priority = Priority.Normal,
        host = None,
        deviceName = Some("my device"),
        source = Some("datadog"),
        tags = Seq("project:scaladog"),
        comments = Seq(
          CommentInfo(5074766851559694338L, AlertType.UserUpdate, Instant.ofEpochSecond(1566742541L))
        ),
        children = Seq(
          ChildEvent(5074764569206621403L, AlertType.Info, Instant.ofEpochSecond(1566742405L)),
          ChildEvent(5074764741922631056L, AlertType.Info, Instant.ofEpochSecond(1566742415L))
        )
      ),
      Event(
        id = 5074756651795870425L,
        text = "a test text 2.",
        dateHappened = Instant.ofEpochSecond(1566741933L),
        title = Some("a test event 2"),
        alertType = AlertType.Info,
        priority = Priority.Low,
        host = None,
        deviceName = None,
        source = Some("My Apps"),
        tags = Seq("tag_key:tag_value"),
        comments = Seq.empty,
        children = Seq.empty
      )
    )

    assert(actual == expect)
  }
}
