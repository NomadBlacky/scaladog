package scaladog

import org.scalatest.FunSpec

class TagTest extends FunSpec {

  describe("apply") {
    it("should bind the empty string to Tag.Value") {
      assert(Tag.apply("") == Tag.Value(""))
    }

    it("should bind a string without ':' to Tag.Value") {
      assert(Tag.apply("the_tag") == Tag.Value("the_tag"))
    }

    it("should bind a string with ':' to Tag.KeyValue") {
      assert(Tag.apply("key:value") == Tag.KeyValue("key", "value"))
    }

    it("should bind a string with multiple ':' to Tag.KeyValue") {
      assert(Tag.apply("key:value:foo") == Tag.KeyValue("key", "value:foo"))
    }
  }

  describe("readwriter") {
    it("should read a string without ':'") {
      val tag = DDPickle.read[Tag]("\"the_tag\"")
      assert(tag == Tag.Value("the_tag"))
    }

    it("should read a string with ':'") {
      val tag = DDPickle.read[Tag]("\"key:value\"")
      assert(tag == Tag.KeyValue("key", "value"))
    }

    it("should write a JSON from Tag.Value") {
      val json = DDPickle.write(Tag.Value("the_tag").asTag)
      assert(json == "\"the_tag\"")
    }

    it("should write a JSON from Tag.KeyValue") {
      val json = DDPickle.write(Tag.KeyValue("key", "value").asTag)
      assert(json == "\"key:value\"")
    }
  }
}
