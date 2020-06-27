package scaladog.api.graphs

import java.net.URL
import java.time.Instant

import requests.Requester
import scaladog.api.{APIClient, APIClientFactory, DDPickle, DatadogSite}

import scala.collection.mutable.ListBuffer

trait GraphsAPIClient extends APIClient {
  def snapshot(
      metricQuery: String,
      start: Instant,
      end: Instant,
      eventQuery: String = null,
      title: String = null
  ): URL
}

object GraphsAPIClient extends APIClientFactory[GraphsAPIClient] {
  override def apply(apiKey: String, appKey: String, site: DatadogSite): GraphsAPIClient =
    new GraphsAPIClientImpl(apiKey, appKey, site)
}

private[graphs] class GraphsAPIClientImpl(
    protected val apiKey: String,
    protected val appKey: String,
    val site: DatadogSite,
    protected val _requester: Option[Requester] = None
) extends GraphsAPIClient {
  override def snapshot(metricQuery: String, start: Instant, end: Instant, eventQuery: String, title: String): URL = {
    val params = ListBuffer(
      "metric_query" -> metricQuery,
      "start" -> start.getEpochSecond.toString,
      "end" -> end.getEpochSecond.toString
    )
    if (eventQuery != null) params += ("event_query" -> eventQuery)
    if (title != null) params += ("title" -> title)

    new URL(httpGet[SnapshotResponse]("/graph/snapshot", params.toSeq).snapshotUrl)
  }
}

private[graphs] case class SnapshotResponse(snapshotUrl: String)

private[graphs] object SnapshotResponse {
  implicit val reader: DDPickle.Reader[SnapshotResponse] = DDPickle.macroR
}
