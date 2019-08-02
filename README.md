# scaladog (WIP)

[Datadog API](https://docs.datadoghq.com/api/) client for Scala.

## Getting Started

Add the dependency.

+ build.sbt

```scala
libraryDependencies += "dev.nomadblacky" %% "scaladog" % "0.1.0"
```

+ or [Ammonite](http://ammonite.io)

```scala
import $ivy.`dev.nomadblacky::scaladog:0.1.0`
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
val client = scaladog.Client("<your api key>", "<your application key>", scaladog.DatadogSite.US)
```

Validate API access.

```scala
val client = scaladog.Client()
assert(client.validate())
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
val client = scaladog.Client()

val response = client.serviceCheck(
  check = "app.is_ok",
  hostName = "app1",
  status = scaladog.ServiceCheckStatus.OK,
  timestamp = java.time.Instant.now(),
  message = "The application is healthy.",
  tags = List("env" -> "prod")
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
val response = client.getMetrics(from, host)

println(response) // GetMetricsResponse(List(test.metric),2019-07-30T15:22:39Z,Some(myhost))
```

#### [Post timeseries points](https://docs.datadoghq.com/api/?lang=bash#post-timeseries-points)

```scala
import scaladog._
import java.time.Instant
import scala.util.Random

val response = scaladog.Client().postMetrics(
  Seq(
    Series(
      metric = "test.metric",
      points = Seq(Point(Instant.now(), Random.nextInt(1000))),
      host = "myhost",
      tags = Seq(Tag("project:scaladog")),
      MetricType.Gauge
    )
  )
)

assert(response.isOk)
```
