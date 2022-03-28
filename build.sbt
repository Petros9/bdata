name := "bdata"

version := "0.1"

scalaVersion := "2.13.8"
val scalaTestV = "3.2.9"
val akkaV = "2.6.17"
val akkaHttpV = "10.2.6"
libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestV % Test
libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.7.8"
libraryDependencies += "org.mockito" %% "mockito-scala-scalatest" % "1.16.46" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % akkaV % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % akkaV % Test
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % Test