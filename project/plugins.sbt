addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.21")
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.2.0")
addSbtPlugin("com.github.sbt" % "sbt-release" % "1.1.0")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "os-lib" % "0.8.1"
)
