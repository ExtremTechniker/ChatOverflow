package de.sebinside.codeoverflow.chatoverflow

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation
import de.sebinside.codeoverflow.chatoverflow.backend.provider.MessageProvider
import de.sebinside.codeoverflow.chatoverflow.backend.provider.mockup.MockUpChat
import de.sebinside.codeoverflow.chatoverflow.backend.provider.twitch.TwitchChat
import de.sebinside.codeoverflow.chatoverflow.backend.provider.youtube.YouTubeChat
import de.sebinside.codeoverflow.chatoverflow.project.ProjectRegistry
import de.sebinside.codeoverflow.chatoverflow.project.displaystatistics.StatisticsDisplayProject
import de.sebinside.codeoverflow.chatoverflow.project.dummyproject.DummyProject
import de.sebinside.codeoverflow.chatoverflow.project.minecraftcontrol.MinecraftControl
import de.sebinside.codeoverflow.chatoverflow.project.olacolorcontroll.{CommunityColor, OlaRedVsBlue}
import de.sebinside.codeoverflow.chatoverflow.project.pizza.WhatPizzaProject
import de.sebinside.codeoverflow.chatoverflow.project.spamfilter.SpamFilterProject
import de.sebinside.codeoverflow.chatoverflow.util.ArgsParser.parse
import org.apache.log4j.Logger

/**
  * The Main Object of the ChatOverflow project. Parses the command line and starts a chat project.
  */
object ChatOverflow {

  private val logger = Logger.getLogger(ChatOverflow.getClass)

  def main(args: Array[String]): Unit = {
    initProjects()

    logger.info("Started Chat Overflow. Initialized Projects.")

    // Parse args and start doing cool stuff!
    parse(args) { config =>

      // Get provider from args
      val provider: Option[MessageProvider] =
        if (!config.twitchChannel.isEmpty) {
          logger.info("Uses twitch chat with channel id \"%s\".".format(config.twitchChannel))
          Some(TwitchChat(config.twitchChannel))
        } else if (!config.youtubeLiveStreamId.isEmpty) {
          logger.info("Uses youtube chat with stream id \"%s\".".format(config.youtubeLiveStreamId))
          Some(YouTubeChat(config.youtubeLiveStreamId))
        } else if (!config.mockUpChatInputFile.isEmpty) {
          logger.info("Uses mockup chat with file \"%s\".".format(config.mockUpChatInputFile))
          Some(MockUpChat(config.mockUpChatInputFile))
        } else {
          logger.fatal("No chat information provided. Use --help to get further information.")
          None
        }

      if (config.listProjects) {

        logger.info("Available Projects:\n%s".format(ProjectRegistry.listProjects.mkString(", ")))
        System.exit(0)

      } else if (!ProjectRegistry.exists(config.projectName)) {

        // Did not find project
        logger.fatal("Unable to find project with name \"%s\".".format(config.projectName))
        System.exit(1)

      } else if (provider.isEmpty) {

        logger.fatal("No twitch irc channel, youtube livestream id or mockup chat file provided.")
        System.exit(1)

      } else {

        // Start doing cool stuff!
        logger.info("Starting chat project: \"%s\".".format(config.projectName))
        logger.info("Provided arguments: %s".format(config.arguments.mkString(", ")))

        ProjectRegistry.start(config.projectName, ChatEvaluation(provider.get), config.arguments)
      }
    }
  }

  private def initProjects(): Unit = ProjectRegistry.registerAll(Seq(
    DummyProject(),
    OlaRedVsBlue(),
    MinecraftControl(),
    WhatPizzaProject(),
    CommunityColor(),
    StatisticsDisplayProject(),
    SpamFilterProject()
  ))


}
