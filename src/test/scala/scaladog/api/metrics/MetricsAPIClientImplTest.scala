package scaladog.api.metrics

import java.time.Instant
import java.time.temporal.ChronoUnit

import scaladog.ScaladogUnitTest
import scaladog.api.StatusResponse

class MetricsAPIClientImplTest extends ScaladogUnitTest {
  describe("getMetrics") {
    it("returns a metrics list") {
      val requester = genTestRequester(
        url = "https://api.datadoghq.com/api/v1/check_run",
        statusCode = 200,
        """{
          |    "metrics": [
          |        "system.cpu.guest",
          |        "system.cpu.idle",
          |        "system.cpu.iowait"
          |    ],
          |    "from": "1559347200",
          |    "host": "myhost"
          |}
        """.stripMargin.trim
      )
      val client = MetricsAPIClientImpl(apiKey, appKey, site, Some(requester))

      val actual = client.getMetrics(Instant.now().minus(1, ChronoUnit.DAYS), "myhost")
      val expect = GetMetricsResponse(
        Seq("system.cpu.guest", "system.cpu.idle", "system.cpu.iowait"),
        Instant.ofEpochSecond(1559347200L),
        Some("myhost")
      )
      assert(actual === expect)
    }
  }

  describe("post metrics operations") {

    val requester = genTestRequester(
      url = "https://api.datadoghq.com/api/v1/series",
      statusCode = 200,
      """{
        |    "status":"ok"
        |}
        """.stripMargin.trim
    )
    def newClient = MetricsAPIClientImpl(apiKey, appKey, site, Some(requester))

    describe("postMetrics") {
      it("returns OK when the request is succeeded") {
        assert(newClient.postMetrics(Seq.empty) === StatusResponse("ok"))
      }
    }

    describe("postSingleMetric") {
      val instant = Instant.parse("2020-01-01T00:00:00Z")

      val client = spy(newClient, lenient = true)
      val actual = client.postSingleMetric("example.metrics", 10, instant)

      it("returns OK") {
        assert(actual === StatusResponse("ok"))
      }

      it("calls postMetrics(Seq[Series])") {
        verify(client).postMetrics(Seq(Series("example.metrics", Seq(Point(instant, 10)))))
      }
    }
  }
}
