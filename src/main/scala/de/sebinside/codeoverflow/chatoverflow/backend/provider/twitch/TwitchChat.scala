package de.sebinside.codeoverflow.chatoverflow.backend.provider.twitch

import java.io.File

import de.sebinside.codeoverflow.chatoverflow.ChatMessage
import de.sebinside.codeoverflow.chatoverflow.backend.provider.MessageProvider
import org.pircbotx.cap.EnableCapHandler
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.types.GenericMessageEvent
import org.pircbotx.{Configuration, PircBotX}

import scala.xml.{NodeSeq, XML}

/**
  * Created by seb on 17.12.16.
  */
class TwitchChat(channelName: String) extends MessageProvider {

  // TODO: Read from args if possible
  private val XMLFilePath = "src/main/resources/twitch_login.xml"

  private val loginData = readSshXML(XMLFilePath)
  private val config = new Configuration.Builder()
    .setAutoNickChange(false)
    .setOnJoinWhoEnabled(false)
    .setCapEnabled(true)
    .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
    .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
    .addServer("irc.twitch.tv")
    .setName(loginData._1)
    .setServerPassword(loginData._2)
    .addAutoJoinChannel(channelName)
    .addListener(new IRCListener())
    .buildConfiguration()

  private val bot = new PircBotX(config)

  // TODO: Make async
  println("Starting bot now on channel %s!".format(channelName))
  bot.startBot()

  private def readSshXML(filePath: String): (String, String) = {

    val twitchXML: NodeSeq = XML.loadFile(new File(filePath))

    if (twitchXML.isEmpty)
      throw new Exception("Invalid XML Format. Use {name, oauth} inside of {login}.")

    val twitchName = (twitchXML \ "name").text
    val twitchOauth = (twitchXML \ "oauth").text

    (twitchName, twitchOauth)
  }

  override private[backend] def getMessages = ???

  override private[backend] def getMessages(lastMilliseconds: Long) = List[ChatMessage]()
}

object TwitchChat {
  def apply(channelName: String): TwitchChat = new TwitchChat(channelName)
}

class IRCListener extends ListenerAdapter {
  override def onGenericMessage(event: GenericMessageEvent): Unit = {
    // TODO: Add code to collect messages in a list etc.
    // TODO: Use IRC Caps to receive twitch specific data (badges, etc.)
    println("%s: %s".format(event.getUser.getNick, event.getMessage))
  }

}