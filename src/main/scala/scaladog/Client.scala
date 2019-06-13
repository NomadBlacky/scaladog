package scaladog
import requests.Requester

import scala.util.Try

private[scaladog] class Client(apiKey: String, appKey: String, site: DatadogSite, _requester: Option[Requester]) {

  private[this] val baseUrl = s"https://api.datadoghq.${site.value}/api/v1"

  private def requester(default: Requester) = _requester.getOrElse(default)

  def validate(): Try[Boolean] = Try {
    val response =
      requester(requests.get)
        .apply(s"$baseUrl/validate", params = Seq("api_key" -> apiKey))

    if (response.is2xx) {
      ujson.read(response.text).obj("valid").bool
    } else {
      val errors = ujson.read(response.text).obj("errors").arr
      throw new DatadogApiException(errors.mkString("[", ",", "]"), response)
    }
  }
}

object Client {

  def apply(apiKey: String, appKey: String, site: DatadogSite = DatadogSite.US): Client =
    new Client(apiKey, appKey, site, None)
}
