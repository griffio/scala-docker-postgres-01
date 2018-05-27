name := "scala-db-01"

version := "0.1"

scalaVersion := "2.12.6"

val dockerTestKitVersion = "0.9.6"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

parallelExecution in Test := false

libraryDependencies ++= Seq(
  jdbc,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "ch.qos.logback" % "logback-core" % "1.2.3",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.playframework.anorm" %% "anorm" % "2.6.1",
  "com.whisk" %% "docker-testkit-scalatest" % dockerTestKitVersion % Test,
  "com.whisk" %% "docker-testkit-impl-docker-java" % dockerTestKitVersion % Test,
  "org.postgresql" % "postgresql" % "42.2.2" % Test
)
