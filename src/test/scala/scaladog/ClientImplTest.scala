package scaladog
import java.net.HttpCookie
import java.time.Instant
import java.time.temporal.ChronoUnit

import org.scalatest.FunSpec
import requests._

class ClientImplTest extends FunSpec {

  def genTestClient(url: String, statusCode: Int, body: String) = {
    val response =
      Response(
        url,
        statusCode,
        "MESSAGE",
        Map.empty,
        new ResponseBlob(body.getBytes("utf-8")),
        None
      )
    new ClientImpl("api_key", "app_key", DatadogSite.US, Some(new TestRequester(response)))
  }

  describe("apply default") {

    it("should throw IllegalArgumentException if DATADOG_API_KEY is not found") {
      TestUtils.removeEnv("DATADOG_API_KEY")
      assertThrows[IllegalArgumentException] {
        Client(appKey = "app_key", site = DatadogSite.US)
      }
    }

    it("should throw IllegalArgumentException if DATADOG_APP_KEY is not found") {
      TestUtils.removeEnv("DATADOG_APP_KEY")
      assertThrows[IllegalArgumentException] {
        Client(apiKey = "api_key", site = DatadogSite.US)
      }
    }

    it("should use DatadogSite.US if DATADOG_SITE is not found") {
      TestUtils.removeEnv("DATADOG_SITE")
      val client = Client(apiKey = "api_key", appKey = "app_key")
      assert(client.asInstanceOf[ClientImpl].site == DatadogSite.US)
    }

    it("should throw IllegalArgumentException if DATADOG_SITE is invalid value") {
      TestUtils.setEnv("DATADOG_SITE", "INVALID_SITE")
      assertThrows[IllegalArgumentException] {
        Client(apiKey = "api_key", appKey = "app_key")
      }
    }
  }

  describe("API calls") {

    it("validate") {
      val client = genTestClient(
        url = "https://api.datadoghq.com/api/v1/validate",
        statusCode = 200,
        """{
          |    "valid": true
          |}
        """.stripMargin.trim
      )

      assert(client.validate())
    }

    it("serviceCheck") {
      val client = genTestClient(
        url = "https://api.datadoghq.com/api/v1/check_run",
        statusCode = 200,
        """{
          |    "status":"ok"
          |}
        """.stripMargin.trim
      )

      val actual = client.serviceCheck(check = "app.is_ok", hostName = "myhost", tags = Seq("project:scaladog"))
      assert(actual.isOk)
    }

    it("getMetrics") {
      val client = genTestClient(
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

      val actual = client.getMetrics(Instant.now().minus(1, ChronoUnit.DAYS), "myhost")
      val expect = GetMetricsResponse(
        Seq("system.cpu.guest", "system.cpu.idle", "system.cpu.iowait"),
        Instant.ofEpochSecond(1559347200L),
        Some("myhost")
      )
      assert(actual == expect)
    }

    it("postMetrics") {
      val client = genTestClient(
        url = "https://api.datadoghq.com/api/v1/series",
        statusCode = 200,
        """{
          |    "status":"ok"
          |}
      """.stripMargin.trim
      )

      val actual = client.postMetrics(Seq.empty)
      val expect = StatusResponse("ok")

      assert(actual == expect)
    }
  }

  describe("Error handling") {
    it("should throws DatadogApiException when return a JSON response") {
      val client = genTestClient(
        url = "https://api.datadoghq.com/api/v1/check_run",
        statusCode = 403,
        """{
          |    "errors": [
          |        "Invalid API key"
          |    ]
          |}
        """.stripMargin.trim
      )
      val ex = intercept[DatadogApiException] {
        client.validate()
      }
      assert(ex.getMessage == """403 MESSAGE: ["Invalid API key"]""")
    }

    it("should throws DatadogApiException when return a HTML response") {
      val client = genTestClient(
        url = "https://api.datadoghq.com/api/v1/check_run",
        statusCode = 403,
        """<html>
          |    <body>
          |        <h1>403 Forbidden</h1>
          |Request forbidden by administrative rules.
          |    </body>
          |</html>
        """.stripMargin.trim
      )
      val ex = intercept[DatadogApiException] {
        client.serviceCheck(check = "app.is_ok", hostName = "app1", tags = Seq("project:scaladog"))
      }
      val expect =
        """403 MESSAGE: <html>
          |    <body>
          |        <h1>403 Forbidden</h1>
          |Request forbidden by administrative rules.
          |    </body>
          |</html>
        """.stripMargin.trim
      assert(ex.getMessage == expect)
    }
  }
}

class TestRequester(dummyResponse: Response) extends Requester("METHOD", Session()) {
  override def apply(
      url: String,
      auth: RequestAuth,
      params: Iterable[(String, String)],
      headers: Iterable[(String, String)],
      data: RequestBlob,
      readTimeout: Int,
      connectTimeout: Int,
      proxy: (String, Int),
      cookies: Map[String, HttpCookie],
      cookieValues: Map[String, String],
      maxRedirects: Int,
      verifySslCerts: Boolean,
      autoDecompress: Boolean,
      compress: Compress,
      keepAlive: Boolean
  ): Response = dummyResponse
}
