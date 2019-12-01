# scaladog

[Datadog API](https://docs.datadoghq.com/api/) client for Scala.

[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

## Getting Started

Add the dependency.

+ build.sbt

```scala
libraryDependencies += "dev.nomadblacky" %% "scaladog" % "0.4.2"
```

+ or [Ammonite](http://ammonite.io)

```scala
import $ivy.`dev.nomadblacky::scaladog:0.4.2`
```

Set API key and Application key

+ from environment variables

```bash
export DATADOG_API_KEY=<your api key>
export DATADOG_APP_KEY=<your application key>
export DATADOG_SITE=<your site> # Optional, "US" or "EU", default is "US"
```

```scala
val client = scaladog.Client()
```

+ or manually

```scala
val client = scaladog.Client("<your api key>", "<your application key>", scaladog.api.DatadogSite.US)
```

## Supported APIs

+ [x] Service Checks
+ [ ] Comments
+ [ ] Dashboards
+ [ ] Dashboard Lists
+ [ ] Downtimes
+ [ ] Embeddable graphs
+ [x] Events
+ [ ] Graphs
+ [ ] Hosts
+ [ ] Integration AWS
+ [ ] Integration Azure
+ [ ] Integration GCP
+ [ ] Integration PagerDuty
+ [ ] Integration Slack
+ [ ] Integration Webhooks
+ [ ] Key Management
+ [ ] Logs
+ [ ] Logs Indexes
+ [ ] Metrics
  + [x] `GET /v1/metrics`
  + [x] `POST /v1/series`
  + [ ] `GET /v1/query`
  + [ ] `GET /v1/metrics/<METRIC_NAME>`
  + [ ] `PUT /v1/metrics/<METRIC_NAME>`
  + [ ] `GET /v1/search`
+ [ ] Monitors
+ [ ] Organizations
+ [ ] Screenboards
+ [ ] Synthetics
+ [ ] Tags
+ [ ] Timeboards
+ [ ] Tracing
+ [ ] Usage metering
+ [ ] Users

## Examples

### [Service Checks](https://docs.datadoghq.com/api/?lang=bash#service-checks)

#### [Post a check run](https://docs.datadoghq.com/api/?lang=bash#post-a-check-run)

```scala
import scaladog.api.service_checks.ServiceCheckStatus
import java.time.Instant

val client = scaladog.Client()

val response = client.serviceCheck.postStatus(
  check = "app.is_ok",
  hostName = "app1",
  status = ServiceCheckStatus.OK,
  timestamp = Instant.now(),
  message = "The application is healthy.",
  tags = Seq("env:prod")
)

assert(response.isOk)
```

### [Metrics](https://docs.datadoghq.com/api/?lang=bash#metrics)

#### [Get list of active metrics](https://docs.datadoghq.com/api/?lang=bash#get-list-of-active-metrics)

```scala
import java.time._, temporal._

val client = scaladog.Client()

val from = Instant.now().minus(1, ChronoUnit.DAYS)
val host = "myhost"
val response = client.metrics.getMetrics(from, host)

println(response) // GetMetricsResponse(List(test.metric),2019-07-30T15:22:39Z,Some(myhost))
```

#### [Post timeseries points](https://docs.datadoghq.com/api/?lang=bash#post-timeseries-points)

```scala
import scaladog.api.metrics._
import java.time.Instant
import scala.util.Random

val response = scaladog.Client().metrics.postMetrics(
  Seq(
    Series(
      metric = "test.metric",
      points = Seq(Point(Instant.now(), Random.nextInt(1000))),
      host = "myhost",
      tags = Seq("project:scaladog"),
      MetricType.Gauge
    )
  )
)

assert(response.isOk)
```

## [Events](https://docs.datadoghq.com/api/?lang=bash#events)

### [Post an event](https://docs.datadoghq.com/api/?lang=bash#post-an-event)

```scala
import scaladog.api.events._
import java.time.Instant

val response = scaladog.Client().events.postEvent(
  title = "TEST EVENT",
  text = "This is a test event.",
  dateHappened = Instant.now(),
  priority = Priority.Low,
  tags = Seq("project:scaladog"),
  alertType = AlertType.Info
)

assert(response.isOk)
```

### [Get an event](https://docs.datadoghq.com/api/?lang=bash#get-an-event)

```scala
import scaladog.api.events._
import java.time.Instant

val event = scaladog.Client().events.getEvent(1234567890123456789L)

assert(event.id == 1234567890123456789L)
assert(event.title == "TEST EVENT")
assert(event.text == "This is a test event.")
assert(event.tags == Seq("project:scaladog"))
```

### [Query the event stream](https://docs.datadoghq.com/api/?lang=bash#query-the-event-stream)

```scala
import scaladog.api.events._
import java.time._, temporal._

val now = Instant.now()
val events = scaladog.Client().events.query(start = now.minus(1, ChronoUnit.DAYS), end = now)
```

## [Graphs](https://docs.datadoghq.com/api/?lang=bash#graphs)

### [Graph snapshot](https://docs.datadoghq.com/api/?lang=bash#graph-snapshot)

```scala
import java.time._, temporal._

val now = Instant.now()
val url = scaladog.Client().graphs.snapshot(
  metricQuery = "avg:datadog.estimated_usage.hosts{*}",
  start = now.minus(7, ChronoUnit.DAYS),
  end = now,
  title = getClass.getSimpleName
)
assert(url.toString startsWith "https://p.datadoghq.com/snapshot/view/dd-snapshots-prod/")
```
