package scaladog
import java.net.HttpCookie

import org.scalatest.FunSuite
import requests._

import scala.util.Success

class ClientTest extends FunSuite {

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
    new Client("api_key", "app_key", "site", Some(new TestRequester(response)))
  }

  test("validate") {
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
