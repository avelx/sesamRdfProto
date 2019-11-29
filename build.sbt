name := "sesamRdfProto"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "org.openrdf.sesame" % "sesame-repository-sail" % "4.1.2",
  "org.openrdf.sesame" % "sesame-sail-memory" % "4.1.2"
)