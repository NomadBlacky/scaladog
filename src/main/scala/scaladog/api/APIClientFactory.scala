package scaladog.api

import scaladog.ReadEnv

trait APIClientFactory[T <: APIClient] extends ReadEnv {
  def apply(): T = apply(
    apiKey = readEnv("DATADOG_API_KEY"),
    appKey = readEnv("DATADOG_APP_KEY"),
    site = readEnvSite()
  )

  def apply(apiKey: String, appKey: String, site: DatadogSite): T
}
