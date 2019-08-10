package scaladog.api

import scaladog.ScaladogUnitTest

class DatadogSiteTest extends ScaladogUnitTest {

  describe("fromString") {

    it("""should return Some(US) if pass "us" to the argument""") {
      assert(DatadogSite.fromString("us").contains(DatadogSite.US))
    }

    it("""should return Some(US) if pass "US" to the argument""") {
      assert(DatadogSite.fromString("US").contains(DatadogSite.US))
    }

    it("""should return Some(US) if pass "com" to the argument""") {
      assert(DatadogSite.fromString("com").contains(DatadogSite.US))
    }

    it("""should return Some(US) if pass "COM" to the argument""") {
      assert(DatadogSite.fromString("COM").contains(DatadogSite.US))
    }

    it("""should return Some(EU) if pass "eu" to the argument""") {
      assert(DatadogSite.fromString("eu").contains(DatadogSite.EU))
    }

    it("""should return Some(EU) if pass "EU" to the argument""") {
      assert(DatadogSite.fromString("EU").contains(DatadogSite.EU))
    }
  }
}
