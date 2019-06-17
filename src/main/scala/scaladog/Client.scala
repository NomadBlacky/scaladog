package scaladog
import requests.{Requester, Response}

private[scaladog] class Client(
    apiKey: String,
    appKey: String,
    val site: DatadogSite,
    _requester: Option[Requester]
) {

  private[this] val baseUrl = s"https://api.datadoghq.${site.value}/api/v1"

  private def requester(default: Requester) = _requester.getOrElse(default)

  def validate(): Boolean = {
    val response =
      requester(requests.get)
        .apply(s"$baseUrl/validate", params = Seq("api_key" -> apiKey))

    throwErrorOr(response) { res =>
      ujson.read(res.text).obj("valid").bool
    }
  }

  private def throwErrorOr[T](response: Response)(f: Response => T): T = {
    if (response.is2xx) {
      f(response)
    } else {
      val errors = ujson.read(response.text).obj("errors").arr
      throw new DatadogApiException(errors.mkString("[", ",", "]"), response)
    }
  }
}

object Client {

  def apply(
      apiKey: String = readEnv("DATADOG_API_KEY"),
      appKey: String = readEnv("DATADOG_APP_KEY"),
      site: DatadogSite = readEnvSite()
  ): Client =
    new Client(apiKey, appKey, site, None)

  private def readEnv(key: String): String = {
    sys.env.getOrElse(
      key,
      throw new IllegalArgumentException(s"scaladog initialization failed: Environment variable $key is not found.")
    )
  }

  private def readEnvSite(): DatadogSite =
    sys.env
      .get("DATADOG_SITE")
      .map { env =>
        DatadogSite
          .fromString(env)
          .getOrElse(
            throw new IllegalArgumentException(
              s"scaladog initialization failed: Unsupported site $env"
            )
          )
      }
      .getOrElse(DatadogSite.US)
}
