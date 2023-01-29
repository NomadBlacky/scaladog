import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

val Scala2_13 = "2.13.10"
val Scala2_12 = "2.12.17"

val supportedScalaVersions = List(Scala2_13, Scala2_12)

ThisBuild / scalaVersion := Scala2_13
ThisBuild / organization := "dev.nomadblacky"
ThisBuild / organizationName := "NomadBlacky"

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-Xfatal-warnings",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Wconf:cat=unused-nowarn:s"
  )
)

lazy val scaladog = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "scaladog",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "requests" % "0.8.0",
      "com.lihaoyi" %% "upickle" % "2.0.0",
      "com.beachape" %% "enumeratum" % "1.7.2",
      "org.scalatest" %% "scalatest" % "3.2.15" % Test,
      "org.mockito" %% "mockito-scala-scalatest" % "1.17.12" % Test
    ),
    releaseCrossBuild := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      ReleaseProcesses.setReleaseVersionToReadme,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("+publishSigned"),
      releaseStepCommand("sonatypeBundleRelease"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )

lazy val integrationTests = (project in file("integrationTests"))
  .dependsOn(scaladog % "test->test;compile->compile")
  .settings(commonSettings)
  .settings(
    crossScalaVersions := supportedScalaVersions,
    publishArtifact := false,
    publish := {},
    publishLocal := {}
  )
