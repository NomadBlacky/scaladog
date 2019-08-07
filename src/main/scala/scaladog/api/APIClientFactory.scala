package scaladog.api

import scaladog.DatadogSite

trait APIClientFactory[T <: APIClient] {
  def apply(): T = apply(
    apiKey = readEnv("DATADOG_API_KEY"),
    appKey = readEnv("DATADOG_APP_KEY"),
    site = readEnvSite()
  )

  def apply(apiKey: String, appKey: String, site: DatadogSite): T

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
