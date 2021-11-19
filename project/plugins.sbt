addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.4")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.10")
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.1.2")
addSbtPlugin("com.github.sbt" % "sbt-release" % "1.1.0")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "os-lib" % "0.7.8"
)
