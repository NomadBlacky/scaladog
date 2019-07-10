package scaladog

import java.time.Instant
import java.time.temporal.ChronoUnit

import org.scalatest.Inside

class MetricsAPIIntegrationTest extends ClientITSpec with Inside {

  test("GET /metrics") {
    val from = Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS)
    val host = "myhost"
    val response = client.getMetrics(from, host)
    inside(response) { case GetMetricsResponse(_, actualFrom, actualHost) =>
      assert(actualFrom == from)
      assert(Some(host) == actualHost)
    }
  }
}
