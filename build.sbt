val Scala2_13 = "2.13.0"
val Scala2_12 = "2.12.8"

val supportedScalaVersions = List(Scala2_13, Scala2_12)

ThisBuild / scalaVersion := Scala2_13
ThisBuild / organization := "com.github.nomadblacky"
ThisBuild / organizationName := "NomadBlacky"

lazy val root = (project in file("."))
  .settings(
    name := "scaladog",
    crossScalaVersions := supportedScalaVersions,
    libraryDependencies ++= Seq(
        "com.lihaoyi"   %% "requests"  % "0.2.0",
        "com.lihaoyi"   %% "upickle"   % "0.7.5",
        "org.scalatest" %% "scalatest" % "3.0.8" % Test
      )
  )
