addSbtPlugin("org.scalameta"     % "sbt-scalafmt" % "2.0.7")
addSbtPlugin("org.xerial.sbt"    % "sbt-sonatype" % "3.8")
addSbtPlugin("com.jsuereth"      % "sbt-pgp"      % "2.0.0")
addSbtPlugin("com.github.gseitz" % "sbt-release"  % "1.0.12")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "os-lib" % "0.3.0"
)
