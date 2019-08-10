package scaladog.api.metrics

import java.time.Instant
import java.time.temporal.ChronoUnit

import org.scalatest.Inside
import scaladog.ClientITSpec

import scala.util.Random

class MetricsAPIIntegrationTest extends ClientITSpec with Inside {

  test("GET /metrics") {
    val from = Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS)
    val host = "myhost"
    val response = client.metrics.getMetrics(from, host)
    inside(response) { case GetMetricsResponse(_, actualFrom, actualHost) =>
      assert(actualFrom == from)
      assert(Some(host) == actualHost)
    }
  }

  test("POST /series") {
    val response = client.metrics.postMetrics(
      Seq(
        Series(
          metric = "test.metric",
          points = Seq(Point(Instant.now(), Random.nextInt(1000))),
          host = "myhost",
          tags = Seq("project:scaladog"),
          MetricType.Gauge
        )
      )
    )
    assert(response.isOk)
  }
}
