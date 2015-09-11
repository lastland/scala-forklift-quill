// uncomment this to enable snapshot versions
//resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= List(
  "com.liyaos" %% "scala-forklift-slick" % "0.2.0-ALPHA"
  ,"com.zaxxer" % "HikariCP" % "2.3.9"
)
