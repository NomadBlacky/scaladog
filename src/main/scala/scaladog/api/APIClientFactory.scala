package scaladog.api

import scaladog.ReadEnv

trait APIClientFactory[T <: APIClient] extends ReadEnv {
  def apply(
      apiKey: String = readEnv("DATADOG_API_KEY"),
      appKey: String = readEnv("DATADOG_APP_KEY"),
      site: DatadogSite = readEnvSite()
  ): T
}
