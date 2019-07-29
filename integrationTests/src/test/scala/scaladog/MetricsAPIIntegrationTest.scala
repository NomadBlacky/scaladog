package scaladog

import java.time.Instant
import java.time.temporal.ChronoUnit

import org.scalatest.Inside

import scala.util.Random

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

  test("POST /series") {
    val response = client.postMetrics(
      Seq(
        Series(
          metric = "test.metric",
          points = Seq(Point(Instant.now(), Random.nextInt(1000))),
          host = "myhost",
          tags = Seq(Tag("project:scaladog")),
          MetricType.Gauge
        )
      )
    )
    assert(response.isOk)
  }
}
