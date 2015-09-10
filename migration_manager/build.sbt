// uncomment this to enable snapshot versions
resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= List(
  "com.typesafe.slick" %% "slick" % "3.0.0"
  ,"com.typesafe.slick" %% "slick-codegen" % "3.0.0"
  ,"org.scala-lang" % "scala-compiler" % "2.11.6"
  ,"com.h2database" % "h2" % "1.3.166"
  ,"com.liyaos" %% "scala-forklift-slick" % "0.1.0-SNAPSHOT"
  ,"com.zaxxer" % "HikariCP" % "2.3.9"
)
