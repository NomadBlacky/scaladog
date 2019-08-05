import java.time.Instant

package object scaladog {

  /**
    * http://www.lihaoyi.com/upickle/#CustomConfiguration
    */
  private[scaladog] object DDPickle extends upickle.AttributeTagged {
    private def camelToSnake(s: String) = {
      s.split("(?=[A-Z])", -1).map(_.toLowerCase).mkString("_")
    }
    private def snakeToCamel(s: String) = {
      val res = s.split("_", -1).map(x => s"${x(0).toUpper}${x.drop(1)}").mkString
      s"${s(0).toLower}${res.drop(1)}"
    }

    override def objectAttributeKeyReadMap(s: CharSequence): String =
      snakeToCamel(s.toString)
    override def objectAttributeKeyWriteMap(s: CharSequence): String =
      camelToSnake(s.toString)

    override def objectTypeKeyReadMap(s: CharSequence): String =
      snakeToCamel(s.toString)
    override def objectTypeKeyWriteMap(s: CharSequence): String =
      camelToSnake(s.toString)

    override implicit def OptionWriter[T: Writer]: Writer[Option[T]] =
      implicitly[Writer[T]].comap[Option[T]] {
        case None    => null.asInstanceOf[T]
        case Some(x) => x
      }

    override implicit def OptionReader[T: Reader]: Reader[Option[T]] =
      implicitly[Reader[T]].mapNulls {
        case null => None
        case x    => Some(x)
      }
  }

  import DDPickle._

  private[scaladog] implicit val instantRW: ReadWriter[Instant] =
    readwriter[String].bimap(_.getEpochSecond.toString, s => Instant.ofEpochSecond(s.toLong))
}
