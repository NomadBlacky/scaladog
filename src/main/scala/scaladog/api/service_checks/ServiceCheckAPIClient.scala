package scaladog.api.service_checks

import java.time.Instant

import requests.Requester
import scaladog.api.{APIClient, APIClientFactory}
import scaladog.{DatadogSite, StatusResponse}

trait ServiceCheckAPIClient extends APIClient {
  def serviceCheck(
      check: String,
      hostName: String,
      status: ServiceCheckStatus = ServiceCheckStatus.OK,
      timestamp: Instant = Instant.now(),
      message: String = "",
      tags: Seq[String] = Seq.empty
  ): StatusResponse
}

object ServiceCheckAPIClient extends APIClientFactory[ServiceCheckAPIClient] {
  override def apply(apiKey: String, appKey: String, site: DatadogSite): ServiceCheckAPIClient =
    ServiceCheckAPIClientImpl(apiKey, appKey, site)
}

private[service_checks] final case class ServiceCheckAPIClientImpl(
    apiKey: String,
    appKey: String,
    site: DatadogSite,
    _requester: Option[Requester] = None
) extends ServiceCheckAPIClient {
  override def serviceCheck(
      check: String,
      hostName: String,
      status: ServiceCheckStatus,
      timestamp: Instant,
      message: String,
      tags: Seq[String]
  ): StatusResponse =
    httpPost("/check_run", ServiceCheckRequest(check, hostName, status, timestamp, message, tags))
}
