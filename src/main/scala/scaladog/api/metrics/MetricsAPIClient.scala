package scaladog.api.metrics

import java.time.Instant

import requests.Requester
import scaladog.api.{APIClient, APIClientFactory, DatadogSite, StatusResponse}

trait MetricsAPIClient extends APIClient {
  def getMetrics(from: Instant, host: String = ""): GetMetricsResponse
  def postMetrics(series: Seq[Series]): StatusResponse
}

object MetricsAPIClient extends APIClientFactory[MetricsAPIClient] {
  def apply(apiKey: String, appKey: String, site: DatadogSite): MetricsAPIClient =
    MetricsAPIClientImpl(apiKey, appKey, site)
}

private[metrics] final case class MetricsAPIClientImpl(
    apiKey: String,
    appKey: String,
    site: DatadogSite,
    _requester: Option[Requester] = None
) extends MetricsAPIClient {

  def getMetrics(from: Instant, host: String = ""): GetMetricsResponse =
    httpGet[GetMetricsResponse]("/metrics", Seq("from" -> from.getEpochSecond.toString, "host" -> host))

  def postMetrics(series: Seq[Series]): StatusResponse =
    httpPost[PostMetricsRequest, StatusResponse]("/series", PostMetricsRequest(series))

}
