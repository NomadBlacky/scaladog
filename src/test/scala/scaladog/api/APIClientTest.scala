package scaladog.api

import requests.Requester
import scaladog.ScaladogUnitTest

class APIClientTest extends ScaladogUnitTest {

  describe("Error handling") {
    it("should throws DatadogApiException when return a JSON response") {
      val requester = genTestRequester(
        url = "https://api.datadoghq.com/api/v1/check_run",
        statusCode = 403,
        """{
          |    "errors": [
          |        "Invalid API key"
          |    ]
          |}
        """.stripMargin.trim
      )
      val client = DummyAPIClient(requester)
      val ex = intercept[DatadogApiException] {
        client.request()
      }
      assert(ex.getMessage == """403 MESSAGE: ["Invalid API key"]""")
    }

    it("should throws DatadogApiException when return a HTML response") {
      val requester = genTestRequester(
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
      val client = DummyAPIClient(requester)
      val ex = intercept[DatadogApiException] {
        client.request()
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

case class DummyAPIClient(requester: Requester) extends APIClient {
  protected val apiKey: String                = "apiKey"
  protected val appKey: String                = "appKey"
  val site: DatadogSite                       = DatadogSite.US
  protected val _requester: Option[Requester] = Some(requester)

  def request(): DummyResponse = httpGet[DummyResponse]("/foo", Seq.empty)
}

case class DummyResponse(state: String)

object DummyResponse {
  implicit val reader: DDPickle.Reader[DummyResponse] = DDPickle.macroR
}
