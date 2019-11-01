package scaladog
import scaladog.api.DatadogSite
import scaladog.api.events.EventsAPIClient
import scaladog.api.graphs.GraphsAPIClient
import scaladog.api.metrics.MetricsAPIClient
import scaladog.api.service_checks.ServiceCheckAPIClient

trait Client {
  def metrics: MetricsAPIClient
  def serviceCheck: ServiceCheckAPIClient
  def events: EventsAPIClient
  def graphs: GraphsAPIClient
}

object Client extends ReadEnv {
  def apply(
      apiKey: String = readEnv("DATADOG_API_KEY"),
      appKey: String = readEnv("DATADOG_APP_KEY"),
      site: DatadogSite = readEnvSite()
  ): Client =
    new ClientImpl(apiKey, appKey, site)
}

private[scaladog] class ClientImpl(
    apiKey: String,
    appKey: String,
    val site: DatadogSite
) extends Client {
  val metrics: MetricsAPIClient           = MetricsAPIClient(apiKey, appKey, site)
  val serviceCheck: ServiceCheckAPIClient = ServiceCheckAPIClient(apiKey, appKey, site)
  val events: EventsAPIClient             = EventsAPIClient(apiKey, appKey, site)
  val graphs: GraphsAPIClient             = GraphsAPIClient(apiKey, appKey, site)
}
