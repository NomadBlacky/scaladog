package scaladog

import java.time.Instant

class ServiceChecksAPIIntegrationTest extends ClientITSpec {

  test("post") {
    val response = client.serviceCheck(
      check = "app.is_ok",
      hostName = "app",
      status = scaladog.ServiceCheckStatus.OK,
      timestamp = Instant.now(),
      tags = List("env:integration_test", "project:scaladog")
    )

    assert(response.isOk)
  }
}
