import com.typesafe.sbt.digest.Import.{DigestKeys, digest}
import com.typesafe.sbt.gzip.Import.gzip
import sbt._
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbtbuildinfo.BuildInfoPlugin.autoImport._
import com.typesafe.sbt.web.Import.Assets
import com.typesafe.sbt.web.SbtWeb.autoImport.pipelineStages
import webscalajs.WebScalaJS.autoImport.scalaJSPipeline
import play.sbt.PlayImport.guice

object Settings {

//  val name = "akkoid-akka"
  val org = "com.akkoid"
//  val version = "1.0.0-SNAPSHOT"

  val scalacOptions = Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-feature"
  )

  object versions {
    val scala = "2.12.4"
    val scalaDom = ""
    val autowire = "0.2.6"
    val upickle = "0.6.6"
    val diode = "1.13"
    val uTest = ""

    val jQuery = "1.11.1"
    val bootstrap = "3.3.6"
    val scalajsScripts = "1.1.2"

    val akkaVersioin = "2.5.12"
    val akkaHttp = "10.1.1"
  }

  /**
    * These dependencies are shared between JS and JVM projects
    * the special %%% function selects the correct version for each project
    */
  val sharedDependencies1 = Def.setting(Seq(
    "com.lihaoyi" %% "autowire" % versions.autowire,
    // https://mvnrepository.com/artifact/com.lihaoyi/upickle
    "com.lihaoyi" %% "upickle" % versions.upickle

  ))

  /** Dependencies only used by the JVM project */
  val jvmDependencies = Def.setting(Seq(
    "com.typesafe.akka" %% "akka-http" % versions.akkaHttp,
    "com.typesafe.akka" %% "akka-stream" % versions.akkaVersioin,
    "com.typesafe.akka" %% "akka-actor" % versions.akkaVersioin,
    "com.vmunier" %% "scalajs-scripts" % versions.scalajsScripts
  ))

  /** Dependencies only used by the JS project (note the use of %%% instead of %%) */
  val scalajsDependencies = Def.setting(Seq(
    "org.scala-js" %% "scalajs-dom" % "0.9.5"
  ))

  /** Dependencies for external JS libs that are bundled into a single .js file according to dependency order */
  val jsDependencies = Def.setting(Seq(
//    "org.webjars" % "jquery" % versions.jQuery / "jquery.js" minified "jquery.min.js",
//    "org.webjars" % "bootstrap" % versions.bootstrap / "bootstrap.js" minified "bootstrap.min.js" dependsOn "jquery.js"
  ))

  lazy val organizationSettings = Seq(
    organization := org
  )

  lazy val buildInfoSettings = Seq(
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoOptions += BuildInfoOption.BuildTime,
    buildInfoOptions += BuildInfoOption.ToJson,
    buildInfoPackage += "com.akkoid.version"
  )

  lazy val serverSettings: Seq[Def.Setting[_]] = Def.settings(
    buildInfoSettings,
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    DigestKeys.indexPath := Some("javascripts/versioned.js"),
    DigestKeys.indexWriter ~= {writer => index => s"var versioned = ${writer(index)}"}

  )

  lazy val serverDependencies: Seq[Def.Setting[_]] = Def.settings(
    libraryDependencies ++= Seq(
      guice
    )
  )

  lazy val clientSettings: Seq[Def.Setting[_]] = Def.settings(

  )

  lazy val clientDependencies: Seq[Def.Setting[_]] = Def.settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.3"
    )
  )

  lazy val clientNPMDependencies = Def.setting(Seq(
      "react" -> "16.4.0",
      "react-dom" -> "16.4.0"
    )
  )

  lazy val jsSettings: Seq[Def.Setting[_]] = Seq(

  )

  lazy val jvmSettings: Seq[Def.Setting[_]] = Seq(

  )

  def sharedSettings(module: Option[String] = None): Seq[Def.Setting[_]] = Seq(
    scalaVersion := versions.scala
  )

  lazy val sharedDependencies: Seq[Def.Setting[_]] = Def.settings(
    libraryDependencies ++= Seq(

    )
  )


}
