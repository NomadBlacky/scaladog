package scaladog.api.metrics

import java.time.Instant
import java.time.temporal.ChronoUnit

import org.scalatest.FunSpec
import scaladog.ScaladogUnitTest
import scaladog.api.StatusResponse

class MetricsAPIClientImplTest extends FunSpec with ScaladogUnitTest {

  describe("getMetrics") {
    it("getMetrics") {
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
      assert(actual == expect)
    }

    it("postMetrics") {
      val requester = genTestRequester(
        url = "https://api.datadoghq.com/api/v1/series",
        statusCode = 200,
        """{
          |    "status":"ok"
          |}
        """.stripMargin.trim
      )
      val client = MetricsAPIClientImpl(apiKey, appKey, site, Some(requester))

      val actual = client.postMetrics(Seq.empty)
      val expect = StatusResponse("ok")

      assert(actual == expect)
    }
  }
}
