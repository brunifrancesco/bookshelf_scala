name := """bookshelf"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

libraryDependencies ++= Seq(
  "com.jason-goodwin" %% "authentikat-jwt" % "0.4.1"
)

// only for Play 2.3.x
libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23"
)

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.10"
)

libraryDependencies += "io.really" %% "jwt-scala" % "1.2.2"
