package scaladog
import java.net.HttpCookie

import org.scalatest.FunSpec
import requests._

import scala.util.Success

class ClientTest extends FunSpec {

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
    new Client("api_key", "app_key", DatadogSite.US, Some(new TestRequester(response)))
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
      assert(client.site == DatadogSite.US)
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

      assert(client.validate() == Success(true))
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
