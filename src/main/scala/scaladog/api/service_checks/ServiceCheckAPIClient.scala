package scaladog.api.service_checks

import java.time.Instant

import requests.Requester
import scaladog.api.{APIClient, APIClientFactory, DatadogSite, StatusResponse}

trait ServiceCheckAPIClient extends APIClient {
  def postStatus(
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
  override def postStatus(
      check: String,
      hostName: String,
      status: ServiceCheckStatus,
      timestamp: Instant,
      message: String,
      tags: Seq[String]
  ): StatusResponse =
    httpPost[ServiceCheckRequest, StatusResponse](
      "/check_run",
      ServiceCheckRequest(check, hostName, status, timestamp, message, tags)
    )
}
