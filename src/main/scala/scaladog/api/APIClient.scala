package scaladog.api

import requests.{RequestBlob, Requester}
import scaladog.api.DDPickle._

import scala.util.Try

trait APIClient {
  protected def apiKey: String
  protected def appKey: String
  def site: DatadogSite
  protected def _requester: Option[Requester] // for unit tests

  private def defaultParams  = Seq("api_key"      -> apiKey, "application_key" -> appKey)
  private val defaultHeaders = Seq("Content-Type" -> "application/json")

  protected def baseUrl = s"https://api.datadoghq.${site.domain}/api/v1"

  protected def httpGet[Res: Reader](path: String, params: Seq[(String, String)]): Res =
    request[Res](_requester.getOrElse(requests.get), path, params, RequestBlob.EmptyRequestBlob)

  protected def httpPost[Req: Writer, Res: Reader](path: String, data: Req): Res =
    request[Res](_requester.getOrElse(requests.post), path, Seq.empty, write(data))

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
