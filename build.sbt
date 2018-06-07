import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import Settings._

lazy val akkoidRoot = project.in(file("."))
  .aggregate(sharedJvm, sharedJs, server, client)
  .settings(organizationSettings)
  .settings(
    publish := {},
    publishLocal := {},
    publishArtifact := false,
    isSnapshot := true,
    run := {
      (run in server in Compile).evaluated
    }
  )

lazy val server = (project in file("server"))
  .settings(scalaJSProjects := Seq(client))
  .settings(sharedSettings(Some("server")))
  .settings(serverSettings)
  .settings(serverDependencies)
  .settings(jvmSettings)
  .enablePlugins(PlayScala, BuildInfoPlugin, WebScalaJSBundlerPlugin)
  .dependsOn(sharedJvm)

lazy val client = (project in file("client"))
  .settings(sharedSettings(Some("client")))
  .settings(clientSettings)
  .settings(clientDependencies)
  .settings(npmDependencies in Compile ++= clientNPMDependencies.value)
  .settings(jsSettings)
  .enablePlugins(ScalaJSWeb, ScalaJSBundlerPlugin)
  .dependsOn(sharedJs)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(sharedSettings())
  .settings(jsSettings)
  .settings(sharedDependencies)
  .jvmSettings(jvmSettings)
  .jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js




