addSbtPlugin("org.scalameta"     % "sbt-scalafmt" % "2.3.0")
addSbtPlugin("org.xerial.sbt"    % "sbt-sonatype" % "3.8.1")
addSbtPlugin("com.jsuereth"      % "sbt-pgp"      % "2.0.1")
addSbtPlugin("com.github.gseitz" % "sbt-release"  % "1.0.13")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "os-lib" % "0.6.3"
)
