package de.sebinside.codeoverflow.chatoverflow.backend.provider.twitch

import java.io.File
import java.util.Calendar

import de.sebinside.codeoverflow.chatoverflow.ChatMessage
import de.sebinside.codeoverflow.chatoverflow.backend.provider.MessageProvider
import org.pircbotx.cap.EnableCapHandler
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.types.GenericMessageEvent
import org.pircbotx.{Configuration, PircBotX}

import scala.collection.mutable.ListBuffer
import scala.xml.{NodeSeq, XML}

class TwitchChat(channelName: String) extends ListenerAdapter with MessageProvider {

  // TODO: Read from args if possible
  private val XMLFilePath = "src/main/resources/twitch_login.xml"

  private val messages: ListBuffer[ChatMessage] = ListBuffer[ChatMessage]()
  private val bot = new PircBotX(getConfig)

  startBotAsync()

  // Constructor End ---------------------

  override def onGenericMessage(event: GenericMessageEvent): Unit = {
    // FIXME: Maybe not the best method. But should not make a huge difference :)
    val currentTime = Calendar.getInstance.getTimeInMillis

    // TODO: Get CAP Tag
    val msg: ChatMessage = new ChatMessage(event.getMessage, event.getUser.getNick, false, currentTime)

    messages += msg
  }

  private def startBotAsync(): Unit = {
    new Thread(new Runnable {
      override def run(): Unit = bot.startBot()
    }).start()
  }

  private def getConfig: Configuration = {
    val loginData = readLoginXML(XMLFilePath)
    new Configuration.Builder()
      .setAutoNickChange(false)
      .setOnJoinWhoEnabled(false)
      .setCapEnabled(true)
      .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
      .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
      .addServer("irc.twitch.tv")
      .setName(loginData._1)
      .setServerPassword(loginData._2)
      .addAutoJoinChannel(channelName)
      .addListener(this)
      .buildConfiguration()
  }

  private def readLoginXML(filePath: String): (String, String) = {

    val twitchXML: NodeSeq = XML.loadFile(new File(filePath))

    if (twitchXML.isEmpty)
      throw new Exception("Invalid XML Format. Use {name, oauth} inside of {login}.")

    val twitchName = (twitchXML \ "name").text
    val twitchOauth = (twitchXML \ "oauth").text

    (twitchName, twitchOauth)
  }

  override private[backend] def getMessages = messages.toList

  override private[backend] def getMessages(lastMilliseconds: Long) = {
    val currentTime = Calendar.getInstance.getTimeInMillis

    messages.filter(_.timestamp > currentTime - lastMilliseconds).toList
  }
}

object TwitchChat {
  def apply(channelName: String): TwitchChat = new TwitchChat(channelName)
}