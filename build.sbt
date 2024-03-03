import org.jetbrains.sbtidea.Keys.*


lazy val dependencies = Seq(
  "com.theokanning.openai-gpt3-java" % "service" % "0.18.2",
)

// https://github.com/JetBrains/sbt-idea-plugin
lazy val askGptPlugin =
  project.in(file("."))
    .enablePlugins(SbtIdeaPlugin)
    .settings(
      version := "0.0.3-SNAPSHOT",
      scalaVersion := "2.13.13",
      ThisBuild / intellijPluginName := "Ask-GPT",
      //ThisBuild / intellijBuild := "233.14475.28",
      ThisBuild / intellijPlatform := IntelliJPlatform.IdeaCommunity,
      Global / intellijAttachSources := true,
      Compile / javacOptions ++= "--release" :: "17" :: Nil,
      intellijPlugins += "com.intellij.properties".toPlugin,
      libraryDependencies ++= dependencies,
      Compile / unmanagedResourceDirectories += baseDirectory.value / "resources",
      Test / unmanagedResourceDirectories += baseDirectory.value / "testResources"
    )
