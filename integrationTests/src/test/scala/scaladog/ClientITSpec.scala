package scaladog

import org.scalatest.funsuite.AnyFunSuiteLike

trait ClientITSpec extends AnyFunSuiteLike {
  lazy val client = scaladog.Client()
}
