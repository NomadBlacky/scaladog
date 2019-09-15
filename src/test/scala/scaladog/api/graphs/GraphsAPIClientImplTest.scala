package scaladog.api.graphs

import java.net.URL
import java.time.Instant
import java.time.temporal.ChronoUnit

import scaladog.ScaladogUnitTest

class GraphsAPIClientImplTest extends ScaladogUnitTest {
  it("snapshot") {
    val url =
      "https://p.datadoghq.com/snapshot/view/dd-snapshots-prod/org_239352/2019-09-10/a61a36a52b538f1e9e3f4248afd4f9xxxxxxxxxx.png"
    val requester = genTestRequester(
      url = "https://api.datadoghq.com/api/v1/graph/snapshot",
      200,
      body = raw"""{
          |    "graph_def": "{\"requests\": [{\"q\": \"avg:aws.billing.estimated_charges{account_id:1234567890}\"}]}",
          |    "snapshot_url": "$url",
          |    "metric_query": "avg:aws.billing.estimated_charges{account_id:1234567890}"
          |}
        """.stripMargin
    )
    val client = new GraphsAPIClientImpl(apiKey, appKey, site, Some(requester))

    val actual = client.snapshot(
      metricQuery = "query",
      start = Instant.now().minus(1, ChronoUnit.DAYS),
      end = Instant.now()
    )

    assert(actual == new URL(url))
  }
}
