package scaladog

private[scaladog] sealed abstract class DatadogSite(val value: String)

object DatadogSite {
  case object US extends DatadogSite("com")
  case object EU extends DatadogSite("eu")

  def fromString(string: String): Option[DatadogSite] = string.toLowerCase match {
    case "us" | "com" => Some(US)
    case "eu" => Some(EU)
    case _ => None
  }
}
