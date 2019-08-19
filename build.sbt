val ScalatraVersion = "2.6.5"

organization := "net.paulboocock"

name := "json-validator-service"

version := "0.1.0"

scalaVersion := "2.12.9"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.9.v20180320",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.json4s"   %% "json4s-jackson" % "3.5.2",
  "org.json4s" %% "json4s-ext" % "3.6.7",
  "com.github.java-json-tools" % "json-schema-validator" % "2.2.10",
  "net.debasishg" %% "redisclient" % "3.10"
)

mainClass in Compile := Some("net.paulboocock.app.JettyEmbedded")

mappings in Docker += file("src/main/webapp/WEB-INF/web.xml") -> "opt/docker/src/main/webapp/WEB-INF/web.xml"
dockerExposedPorts ++= Seq(8080)

enablePlugins(ScalatraPlugin)
enablePlugins(JavaAppPackaging, DockerComposePlugin)

dockerImageCreationTask := (publishLocal in Docker).value