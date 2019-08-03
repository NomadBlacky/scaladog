import sbtrelease.ReleasePlugin.autoImport.{ReleaseKeys, ReleaseStep}

object ReleaseProcesses {
  private[this] val sbtRegex = """^libraryDependencies \+= "([\w\.]+)" %% "([\w]+)" % "([\w\.]+)"$""".r
  private[this] val ammRegex = """^import \$ivy\.`([\w\.]+)::([\w]+):([\w\.]+)`$""".r

  lazy val setReleaseVersionToReadme: ReleaseStep = ReleaseStep(
    state => {
      val (releaseVersion, _) = state.get(ReleaseKeys.versions).getOrElse(sys.error("No versions are set!"))
      val newReadme = os
        .read(os.pwd / "README.md")
        .linesIterator
        .map {
          case sbtRegex(grp, art, _) => s"""libraryDependencies += "$grp" %% "$art" % "$releaseVersion""""
          case ammRegex(grp, art, _) => s"""import $$ivy.`$grp::$art:$releaseVersion`"""
          case line                  => line
        }
        .mkString("\n")
      os.write.over(os.pwd / "README.md", newReadme)
      state
    }
  )

}
