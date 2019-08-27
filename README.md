# scaladog (WIP)

[Datadog API](https://docs.datadoghq.com/api/) client for Scala.

## Getting Started

Add the dependency.

+ build.sbt

```scala
libraryDependencies += "dev.nomadblacky" %% "scaladog" % "0.3.0"
```

+ or [Ammonite](http://ammonite.io)

```scala
import $ivy.`dev.nomadblacky::scaladog:0.3.0`
```

Set API key and Application key

+ from environment variables

```bash
export DATADOG_API_KEY=<your api key>
export DATADOG_APP_KEY=<your application key>
export DATADOG_SITE=<your site> # Optional, "US" or "EU", default is "US"
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
+ [ ] Events
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

## Changelog

### 0.3.0

+ Support Events API (#29)
  + `POST /v1/events`
  + `GET /v1/events/<EVENT_ID>`
  + `GET /v1/events`

### 0.2.0

+ Support part of Metrics API (#9)
  + `GET /v1/metrics`
  + `POST /v1/series`

### 0.1.0

+ Support Service Checks API (#2)
  + `POST /v1/check_run`

### 0.0.1

+ First Release
