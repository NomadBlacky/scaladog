package scaladog.api.events

import java.time.Instant

import scaladog.ClientITSpec

class EventsAPIIntegrationTest extends ClientITSpec {

  test("POST /events") {
    val response = client.events.postEvent(
      title = "TEST EVENT",
      text = "This is a test event.",
      dateHappened = Instant.now(),
      priority = Priority.Low,
      tags = Seq("project:scaladog"),
      alertType = AlertType.Info
    )
    assert(response.isOk)
  }

  test("GET /events/<EVENT_ID>") {
    val actual = client.events.getEvent(5052730002120180683L)
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
