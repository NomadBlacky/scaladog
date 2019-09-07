addSbtPlugin("org.scalameta"     % "sbt-scalafmt" % "2.0.4")
addSbtPlugin("org.xerial.sbt"    % "sbt-sonatype" % "3.6")
addSbtPlugin("com.jsuereth"      % "sbt-pgp"      % "1.1.2")
addSbtPlugin("com.github.gseitz" % "sbt-release"  % "1.0.11")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "os-lib" % "0.3.0"
)
