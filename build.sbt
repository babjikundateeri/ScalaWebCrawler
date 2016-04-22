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

// scala mock lib depedency
val mock = "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test"


lazy val depedencySettings = Seq (
  libraryDependencies += logback,
  libraryDependencies += scalatest,
  libraryDependencies += mock,
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
