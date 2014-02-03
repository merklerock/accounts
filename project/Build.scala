import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "account"
  val appVersion      = "1.0-ALPHA"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "org.mybatis" % "mybatis" % "3.1.1",
    "org.mybatis.scala" % "mybatis-scala-core" % "1.0.0",
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "org.xerial" % "sqlite-jdbc" % "3.7.2",
    "org.apache.shiro" % "shiro-core" % "1.2.2",
    "org.slf4j" % "slf4j-simple" % "1.6.1"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
