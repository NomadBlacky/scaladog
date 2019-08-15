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
}
