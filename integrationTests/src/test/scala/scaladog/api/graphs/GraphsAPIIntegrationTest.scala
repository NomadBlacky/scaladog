package scaladog.api.graphs

import java.time.Instant
import java.time.temporal.ChronoUnit

import scaladog.ClientITSpec

class GraphsAPIIntegrationTest extends ClientITSpec {

  test("GET /graph/snapshot") {
    val now = Instant.now()
    val url = client.graphs.snapshot(
      metricQuery = "avg:datadog.estimated_usage.hosts{*}",
      start = now.minus(7, ChronoUnit.DAYS),
      end = now,
      title = getClass.getSimpleName
    )
    println(url)
    assert(url.toString startsWith "https://p.datadoghq.com/snapshot/view/dd-snapshots-prod/")
  }
}
