
scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.typelevel"  %% "squants"  % "1.3.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)


val circeVersion = "0.8.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
