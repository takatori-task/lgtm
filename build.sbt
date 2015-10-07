name := """lgtm"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  evolutions,
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "com.typesafe.play" % "anorm_2.11" % "2.4.0",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars.bower" % "dropzone" % "4.0.1",
  "org.webjars.bower" % "octicons" % "2.2.3"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


fork in run := true

javaOptions in Test += "-Dconfig.file=conf/test.conf"
