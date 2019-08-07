package scaladog.api

import requests.{RequestBlob, Requester, Response}
import scaladog.DDPickle._
import scaladog.{DatadogApiException, DatadogSite}

import scala.util.Try

trait APIClient {
  val apiKey: String
  val appKey: String
  val site: DatadogSite

  private val defaultParams  = Seq("api_key"      -> apiKey, "application_key" -> appKey)
  private val defaultHeaders = Seq("Content-Type" -> "application/json")

  protected val baseUrl = s"https://api.datadoghq.${site.value}/api/v1"

  /**
    * for unit tests
    */
  protected val _requester: Option[Requester]

  protected def httpGet[Res: Reader](path: String, params: Seq[(String, String)]): Res =
    request(_requester.getOrElse(requests.get), path, params, RequestBlob.EmptyRequestBlob)

  protected def httpPost[Req: Writer, Res: Reader](path: String, data: Req): Res =
    request(_requester.getOrElse(requests.post), path, Seq.empty, write(data))

  private def request[Res: Reader](
      requester: Requester,
      path: String,
      params: Seq[(String, String)],
      data: RequestBlob
  ): Res = {
    val response =
      requester(url = s"$baseUrl$path", params = defaultParams ++ params, headers = defaultHeaders, data = data)
    if (response.is2xx) {
      read[Res](response.text())
    } else {
      val errors = {
        val errorsT = Try(ujson.read(response.text).obj("errors").arr.mkString("[", ",", "]"))
        errorsT.getOrElse(response.text())
      }
      val message = s"${response.statusCode} ${response.statusMessage}: $errors"
      throw new DatadogApiException(message, response)
    }
  }
}
