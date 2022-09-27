import ProjectKeys.{copyManages, copylibs, filesToCross}
import sbt._
import sbt.Keys._
import org.scalafmt.sbt.ScalafmtPlugin.autoImport.{scalafmtOnCompile, scalafmtSbt}
import sbtcrossproject.Platform

object Settings {

  private val depts = Dependencies

  object scalaV {
    val v211    = "2.11.12"
    val v212    = "2.12.15"
    val v213    = "2.13.8"
    val v3      = "3.2.0"
    val v3RC    = "3.2.2-RC1-bin-20220920-b1b1dfd-NIGHTLY"
    val collect = Seq(v211, v212, v213, v3)
  }

  private def scalacOptionsVersion(scalaVersion: String): Seq[String] = {
    val common = Seq("-feature", "-deprecation", "-encoding", "UTF-8")
    val compat = CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, scalaMajor)) => Seq("-Ywarn-dead-code")
      case Some((3, scalaMajor)) => Seq("-Ykind-projector")
      case _                     => Seq.empty
    }
    common ++: compat
  }

  private def genDirectory(sourceFile: File, parVersion: String, exists: Seq[File]): Seq[File] = {
    val fileList = CrossVersion.partialVersion(parVersion) match {
      case Some((2, 11)) =>
        Seq(sourceFile / "scala-2", sourceFile / "scala-2.11", sourceFile / "scala-2.11-2.12")
      case Some((2, 12)) =>
        Seq(
          sourceFile / "scala-2",
          sourceFile / "scala-2.11-2.12",
          sourceFile / "scala-2.12",
          sourceFile / "scala-2.12-2.13"
        )
      case Some((2, 13)) =>
        Seq(sourceFile / "scala-2", sourceFile / "scala-2.12-2.13", sourceFile / "scala-2.13")
      case Some((2, _)) => Seq(sourceFile / "scala-2")
      case Some((3, _)) => Seq(sourceFile / "scala-3")
      case _            => Seq.empty
    }
    val existsPaths = exists.map(_.getCanonicalPath)
    fileList.filterNot(s => existsPaths.exists(t => t == s.getCanonicalPath))
  }

  object org {
    val common = Seq(
      organization         := "net.scalax",
      organizationName     := "Scala Workers",
      organizationHomepage := Some(url("https://github.com/scala-workers"))
    )

    val collect = (version := "0.1.0") +: common

    val testCollect = common
  }

  object all {
    val filesToCrossSetting = Seq(
      filesToCross := Seq.empty,
      filesToCross += baseDirectory.value / "src" / "codegen",
      Compile / filesToCross += baseDirectory.value / "src" / "main",
      Test / filesToCross += baseDirectory.value / "src" / "test"
    )

    val compileFolder =
      (Compile / unmanagedSourceDirectories) ++= (Compile / filesToCross).value
        .flatMap(genDirectory(_, scalaVersion.value, (Compile / unmanagedSourceDirectories).value))
    val testFolder = Test / unmanagedSourceDirectories ++= (Test / filesToCross).value
      .flatMap(genDirectory(_, scalaVersion.value, (Test / unmanagedSourceDirectories).value))

    val collect = Seq(
      Compile / compile := ((Compile / compile) dependsOn (Compile / scalafmtSbt)).value,
      scalafmtOnCompile := true,
      scalacOptions ++= scalacOptionsVersion(scalaVersion.value),
      crossScalaVersions := scalaV.collect,
      compileFolder,
      testFolder,
      depts.kindProjector
    ) ++:
      filesToCrossSetting
  }

  object common {
    val collect = org.collect ++: all.collect
  }

  object forTest {
    val collect = org.testCollect ++: all.collect
  }

  def addFilesToCross(p: Project) = Seq(
    (p / filesToCross) ++= Seq((p / baseDirectory).value / ".." / "shared" / "src" / "codegen"),
    (p / Compile / filesToCross) ++= Seq((p / baseDirectory).value / ".." / "shared" / "src" / "main"),
    (p / Test / filesToCross) ++= Seq((p / baseDirectory).value / ".." / "shared" / "src" / "test")
  )

  val compareModuleID: (ModuleID, Seq[ModuleID]) => Boolean = (a, b) => !b.exists(t => t == a)
  val compareFile: (File, Seq[File]) => Boolean             = (a, b) => !b.map(_.getCanonicalPath).exists(x => x == a.getCanonicalPath)
  def appendSeq[T, U](map: Seq[(Platform, T)], platform: Platform, old: U)(equ: (T, U) => Boolean): Seq[T] = for (
    (p, t) <- map if p == platform && equ(t, old)
  ) yield t

}
