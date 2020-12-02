addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.5")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.1.0")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.13")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "os-lib" % "0.7.1"
)
