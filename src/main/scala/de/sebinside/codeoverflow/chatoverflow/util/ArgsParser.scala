package de.sebinside.codeoverflow.chatoverflow.util

import de.sebinside.codeoverflow.chatoverflow.backend.provider.mockup.MockUpChat
import de.sebinside.codeoverflow.chatoverflow.project.ProjectRegistry

/**
  * Provides utility methods to parse command line input using scopt.
  */
object ArgsParser {

  private val PROGRAM_NAME = "Chat Overflow"
  private val PROGRAM_DESC = "Provides several methods to read and analyze chat entries."

  private val argsParser = new scopt.OptionParser[Config](PROGRAM_NAME) {
    head(PROGRAM_DESC)

    // Chat Provider

    opt[String]('t', "twitch").action((value, config) => config.copy(twitchChannel = value)).
      valueName("<irc channel name>").
      text("The chat channel of a twitch broadcaster. Usually \"#broadcastername\".")

    opt[String]('y', "youtube").action((value, config) => config.copy(youtubeLiveStreamId = value)).
      valueName("<youtube livestream id>").
      text("The youtube id of the livestream. Can be retrieved from the url.")

    opt[String]('m', "mockup").action((value, config) => config.copy(mockUpChatInputFile = value)).
      validate(fileName => if (MockUpChat.getMockUpFile(fileName).exists()) success else
        failure("Unable to locate \"%s\".".format(MockUpChat.getMockUpFile(fileName).getAbsolutePath))).
      valueName("<mockUp filename>").
      text("The filename of a mockUp chat file. Should be located at \"%s\".".format(MockUpChat.MOCKUP_FOLDER))


    // Chat Projects

    opt[String]('p', "project").action((value, config) => config.copy(projectName = value)).
      validate(value => if (ProjectRegistry.exists(value)) success else
        failure("Unable to find a project named \"%s\".".format(value))).
      valueName("<project name>").
      text("The project name to work with the chat input. Get a list of available projects with --list.")

    opt[Map[String, String]]('a', "args").action((value, config) => config.copy(arguments = value)).
      valueName("k1=v1,k2=v2,...").
      text("Optional arguments that will be passed to the chat project.")

    opt[Unit]('l', "list").action((_, config) =>
      config.copy(listProjects = true)).
      text("Get a list of all available chat projects. Exits after print.")

    help("help").hidden().text("prints help")

  }

  /**
    * Parses command line input and executes the code with the given configuration.
    *
    * @param args The command line arguments from the main-method
    * @param code The code to run
    */
  def parse(args: Array[String])(code: Config => Unit): Unit = {

    argsParser.parse(args, Config()) match {
      case None => // argument fail
      case Some(config) => code(config)
    }

  }

  private case class Config(twitchChannel: String = "",
                            youtubeLiveStreamId: String = "",
                            mockUpChatInputFile: String = "",
                            projectName: String = "",
                            listProjects: Boolean = false,
                            arguments: Map[String, String] = Map[String, String]()
                           )


}
