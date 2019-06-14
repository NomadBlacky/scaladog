# scaladog (WIP)

[Datadog API](https://docs.datadoghq.com/api/) client for Scala.

## Getting Started

Add the dependency.

+ build.sbt

```scala
libraryDependencies += "dev.nomadblacky" %% "scaladog" % "0.0.1"
```

+ or [Ammonite](http://ammonite.io)

```scala
import $ivy.`dev.nomadblacky::scaladog:0.0.1`
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
