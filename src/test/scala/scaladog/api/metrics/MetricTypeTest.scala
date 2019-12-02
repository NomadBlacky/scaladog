package scaladog.api.metrics

import scaladog.api.DDPickle
import org.scalatest.funspec.AnyFunSpec

class MetricTypeTest extends AnyFunSpec {
  describe("readwriter") {
    it("should read a string") {
      assert(DDPickle.read[MetricType]("\"rate\"") == MetricType.Rate)
    }

    it("should throw an NoSuchElementException when read an invalid string") {
      assertThrows[NoSuchElementException](DDPickle.read[MetricType]("\"invalid\"") == null)
    }

    it("should write JSON from a string") {
      assert(DDPickle.write(MetricType.Gauge: MetricType) == "\"gauge\"")
    }
  }
}
