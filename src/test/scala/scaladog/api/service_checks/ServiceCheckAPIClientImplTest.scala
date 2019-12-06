package scaladog.api.service_checks

import scaladog.ScaladogUnitTest
import org.scalatest.funspec.AnyFunSpec

class ServiceCheckAPIClientImplTest extends AnyFunSpec with ScaladogUnitTest {
  describe("") {
    val requester = genTestRequester(
      url = "https://api.datadoghq.com/api/v1/check_run",
      statusCode = 200,
      """{
        |    "status":"ok"
        |}
        """.stripMargin.trim
    )
    it("serviceCheck") {
      val client = ServiceCheckAPIClientImpl(
        apiKey,
        appKey,
        site,
        Some(requester)
      )

      val actual = client.postStatus(check = "app.is_ok", hostName = "myhost", tags = Seq("project:scaladog"))
      assert(actual.isOk)
    }
  }
}
