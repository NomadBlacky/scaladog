import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

val Scala3 = "3.0.0"
val Scala2_13 = "2.13.6"
val Scala2_12 = "2.12.14"

val supportedScalaVersions = List(Scala3, Scala2_13, Scala2_12)

ThisBuild / scalaVersion := Scala3
ThisBuild / organization := "dev.nomadblacky"
ThisBuild / organizationName := "NomadBlacky"

lazy val commonSettings = Seq(
  scalacOptions ++= {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((3, _)) =>
        Seq(
          "-deprecation",
          "-feature",
          "-unchecked",
          "-Xfatal-warnings",
          "-source:3.0-migration"
        )
      case _ =>
        Seq(
          "-deprecation",
          "-feature",
          "-unchecked",
          "-Xlint",
          "-Xfatal-warnings",
          "-Ywarn-dead-code",
          "-Ywarn-numeric-widen",
          "-Wconf:cat=unused-nowarn:s",
          "-Xsource:3"
        )
    }
  }
)

lazy val scaladog = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "scaladog",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "requests" % "0.6.9",
      "com.lihaoyi" %% "upickle" % "1.4.0",
      ("com.beachape" %% "enumeratum" % "1.6.1").cross(CrossVersion.for3Use2_13),
      "org.scalatest" %% "scalatest" % "3.2.9" % Test,
      ("org.mockito" %% "mockito-scala-scalatest" % "1.16.37" % Test).cross(CrossVersion.for3Use2_13)
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
