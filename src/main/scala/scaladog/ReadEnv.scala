package scaladog

import scaladog.api.DatadogSite

trait ReadEnv {
  protected def readEnv(key: String): String = {
    sys.env.getOrElse(
      key,
      throw new IllegalArgumentException(s"scaladog initialization failed: Environment variable $key is not found.")
    )
  }

  protected def readEnvSite(): DatadogSite =
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
