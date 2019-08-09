package scaladog.api.service_checks

import java.time.Instant

import scaladog.ClientITSpec

class ServiceChecksAPIIntegrationTest extends ClientITSpec {

  test("post") {
    val response = client.serviceCheck.serviceCheck(
      check = "app.is_ok",
      hostName = "app",
      status = ServiceCheckStatus.OK,
      timestamp = Instant.now(),
      tags = List("env:integration_test", "project:scaladog")
    )

    assert(response.isOk)
  }
}
