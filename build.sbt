import sbt._
import Process._
import Keys._

lazy val commonSettings = Seq (
  name := "ScalaWebCrawler",
  version := "1.0",
  scalaVersion := "2.11.8"
)

// logback for logging
val logback = "ch.qos.logback" %  "logback-classic" % "1.1.7"

// junit dependecy
val junit = "com.novocode" % "junit-interface" % "0.11" % "test"

// scalatest dependecy
val scalatest = "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    // if scala 2.11+ is used, add dependency on scala-xml module
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value ++ Seq(
        "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3",
        "org.scala-lang.modules" %% "scala-swing" % "1.0.1")
    case _ =>
      // or just libraryDependencies.value if you don't depend on scala-swing
      libraryDependencies.value :+ "org.scala-lang" % "scala-swing" % scalaVersion.value
  }
}

lazy val depedencySettings = Seq (
  libraryDependencies += logback,
  libraryDependencies += scalatest,
  libraryDependencies += junit
)

lazy val testOptionSettings = Seq (
  testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v"),  // junit test options
  testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oSD")   // scalatest options
)

lazy val crawler = (project in file(".")).
  settings(commonSettings:_*).
  settings(depedencySettings:_*).
  settings(testOptionSettings:_*)
