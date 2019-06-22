package scaladog

import org.scalatest.FunSuiteLike

trait ClientITSpec extends FunSuiteLike {
  lazy val client = scaladog.Client()
}
