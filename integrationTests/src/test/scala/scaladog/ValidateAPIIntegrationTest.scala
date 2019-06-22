package scaladog

class ValidateAPIIntegrationTest extends ClientITSpec {

  test("validate") {
    assert(client.validate())
  }

}
