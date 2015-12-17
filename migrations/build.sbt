// uncomment this to enable snapshot versions
//resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= List(
  "com.liyaos" %% "scala-forklift-core" % "0.2.0-BETA"
  ,"mysql" % "mysql-connector-java" % "5.1.36"
  ,"io.getquill" %% "quill-jdbc" % "0.1.0"
  ,"com.zaxxer" % "HikariCP" % "2.4.1"
)
