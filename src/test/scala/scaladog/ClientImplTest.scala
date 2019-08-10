package scaladog

import scaladog.api.DatadogSite

class ClientImplTest extends ScaladogUnitTest {

  describe("apply default") {

    it("should throw IllegalArgumentException if DATADOG_API_KEY is not found") {
      removeEnv("DATADOG_API_KEY")
      assertThrows[IllegalArgumentException] {
        Client(appKey = "app_key", site = DatadogSite.US)
      }
    }

    it("should throw IllegalArgumentException if DATADOG_APP_KEY is not found") {
      removeEnv("DATADOG_APP_KEY")
      assertThrows[IllegalArgumentException] {
        Client(apiKey = "api_key", site = DatadogSite.US)
      }
    }

    it("should use DatadogSite.US if DATADOG_SITE is not found") {
      removeEnv("DATADOG_SITE")
      val client = Client(apiKey = "api_key", appKey = "app_key")
      assert(client.asInstanceOf[ClientImpl].site == DatadogSite.US)
    }

    it("should throw IllegalArgumentException if DATADOG_SITE is invalid value") {
      setEnv("DATADOG_SITE", "INVALID_SITE")
      assertThrows[IllegalArgumentException] {
        Client(apiKey = "api_key", appKey = "app_key")
      }
    }
  }

}
