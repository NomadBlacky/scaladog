package scaladog

import java.net.HttpCookie

import org.mockito.scalatest.MockitoSugar
import org.scalatest.funspec.AnyFunSpec
import requests._
import scaladog.api.DatadogSite

trait ScaladogUnitTest extends AnyFunSpec with MockitoSugar {
  val apiKey = "api_key"
  val appKey = "app_key"
  val site = DatadogSite.US

  def genTestRequester(url: String, statusCode: Int, body: String): Requester = {
    new TestRequester(
      Response(
        url,
        statusCode,
        "MESSAGE",
        new geny.Bytes(body.getBytes("utf-8")),
        Map.empty,
        None
      )
    )
  }

  // https://stackoverflow.com/a/34030731
  def setEnv(key: String, value: String): Unit = {
    val field = System.getenv().getClass.getDeclaredField("m")
    field.setAccessible(true)
    val map = field.get(System.getenv()).asInstanceOf[java.util.Map[java.lang.String, java.lang.String]]
    map.put(key, value)
  }

  def removeEnv(key: String): Unit = {
    val field = System.getenv().getClass.getDeclaredField("m")
    field.setAccessible(true)
    val map = field.get(System.getenv()).asInstanceOf[java.util.Map[java.lang.String, java.lang.String]]
    map.remove(key)
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
      keepAlive: Boolean,
      check: Boolean,
      chunkedUpload: Boolean
  ): Response = dummyResponse
}
