package scaladog
import java.time.Instant

import requests.{Requester, Response}

import scala.util.Try

trait Client {
  def validate(): Boolean
  def serviceCheck(
      check: String,
      hostName: String,
      status: ServiceCheckStatus = ServiceCheckStatus.OK,
      timestamp: Instant = Instant.now(),
      message: String = "",
      tags: Iterable[(String, String)] = Iterable.empty
  ): StatusResponse
}

private[scaladog] class ClientImpl(
    apiKey: String,
    appKey: String,
    val site: DatadogSite,
    _requester: Option[Requester] // for unit tests
) extends Client {

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

  def serviceCheck(
      check: String,
      hostName: String,
      status: ServiceCheckStatus = ServiceCheckStatus.OK,
      timestamp: Instant = Instant.now(),
      message: String = "",
      tags: Iterable[(String, String)] = Iterable.empty
  ): StatusResponse = {
    val bodyJson = ujson.Obj(
      "check"     -> check,
      "host_name" -> hostName,
      "status"    -> status.value,
      "timestamp" -> timestamp.getEpochSecond,
      "message"   -> message,
      "tags"      -> tags.map { case (key, value) => s"$key:$value" }
    )
    val response = requester(requests.post)
      .apply(
        url = s"$baseUrl/check_run",
        params = Seq("api_key" -> apiKey),
        headers = Iterable(Client.jsonHeader),
        data = ujson.write(bodyJson)
      )

    throwErrorOr(response) { res =>
      StatusResponse(ujson.read(res.text).obj("status").str)
    }
  }

  private def throwErrorOr[T](response: Response)(f: Response => T): T = {
    if (response.is2xx) {
      f(response)
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

object Client {

  private[scaladog] val jsonHeader = "Content-Type" -> "application/json"

  def apply(
      apiKey: String = readEnv("DATADOG_API_KEY"),
      appKey: String = readEnv("DATADOG_APP_KEY"),
      site: DatadogSite = readEnvSite()
  ): Client =
    new ClientImpl(apiKey, appKey, site, None)

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
