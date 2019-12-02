package scaladog.api.metrics

import java.time.Instant

import scaladog.api.DDPickle
import org.scalatest.funspec.AnyFunSpec

class SeriesTest extends AnyFunSpec {
  describe("writer") {
    it("should serialize Series to JSON object") {
      val series = Series(
        metric = "app.test",
        points = Seq(
          Point(Instant.ofEpochSecond(1559347200L), 10.5)
        ),
        host = "myhost",
        tags = Seq("key:value"),
        metricType = MetricType.Rate
      )
      val actual = DDPickle.write(series, 2)
      val expect =
        """{
          |  "metric": "app.test",
          |  "points": [
          |    [
          |      "1559347200",
          |      "10.5"
          |    ]
          |  ],
          |  "host": "myhost",
          |  "tags": [
          |    "key:value"
          |  ],
          |  "type": "rate"
          |}
        """.stripMargin.trim

      assert(actual == expect)
    }
  }
}
