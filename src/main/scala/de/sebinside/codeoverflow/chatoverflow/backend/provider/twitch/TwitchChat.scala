package de.sebinside.codeoverflow.chatoverflow.backend.provider.twitch

import java.io.File
import java.util.Calendar

import de.sebinside.codeoverflow.chatoverflow.ChatMessage
import de.sebinside.codeoverflow.chatoverflow.backend.provider.MessageProvider
import org.pircbotx.cap.EnableCapHandler
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.MessageEvent
import org.pircbotx.{Configuration, PircBotX}

import scala.collection.mutable.ListBuffer
import scala.xml.{NodeSeq, XML}

/**
  * A TwitchChat represents the irc chat of a twitch channel.
  * The Twitch IRC Login Parameters must be in '''''twitch_login.xml''''' in the resources folder.
  *
  * {{{
  * <login>
  *   <name>Your Twitch Name</name>
  *   <oauth>Your Twitch Login OAuth Params</oauth>
  * </login>
  * }}}
  *
  * @param channelName The channel name of the streamer (must not be identical to the provided login data)
  * @see https://github.com/justintv/Twitch-API/blob/master/IRC.md
  */
class TwitchChat(channelName: String) extends ListenerAdapter with MessageProvider {

  private val XMLFilePath = "src/main/resources/twitch_login.xml"

  private val messages: ListBuffer[ChatMessage] = ListBuffer[ChatMessage]()
  private val bot = new PircBotX(getConfig)

  startBotAsync()

  // Constructor End ---------------------

  /** *
    * Overrides the onMessage()-Method of a ListenerAdapter. Handles incoming chat messages by adding them to a list.
    */
  private[twitch] override def onMessage(event: MessageEvent): Unit = {
    val color = if (event.getV3Tags.get("color").contains("#")) Some[String](event.getV3Tags.get("color")) else None
    val isSub = if (event.getV3Tags.get("subscriber") == "1") true else false
    val msg: ChatMessage = new ChatMessage(event.getMessage, event.getUser.getNick, event.getTimestamp, isSub, color)

    messages += msg
  }

  /**
    * Starts the pIRCBotX-Bot asynchronously in a new thread.
    */
  private def startBotAsync(): Unit = {
    new Thread(new Runnable {
      override def run(): Unit = bot.startBot()
    }).start()
  }

  /**
    * Returns a config for the pIRCBotX-Bot. Reads the login data using [[readLoginXML]]
    *
    * @return Returns a pIRCBotX-Configuration, optimized for twitch
    */
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

  /**
    * Reads the login data from a xml file. The format is described in [[TwitchChat]]
    *
    * @param filePath A relative file path to a xml-file.
    * @return A tuple with username and oauth-password
    */
  private def readLoginXML(filePath: String): (String, String) = {

    val twitchXML: NodeSeq = XML.loadFile(new File(filePath))

    if (twitchXML.isEmpty)
      throw new Exception("Invalid XML Format. Use {name, oauth} inside of {login}.")

    val twitchName = (twitchXML \ "name").text
    val twitchOauth = (twitchXML \ "oauth").text

    (twitchName, twitchOauth)
  }

  /**
    * Returns all twitch chat messages since the creation of the chat object.
    *
    * @return A list of ChatMessages
    */
  override private[backend] def getMessages = messages.toList

  /**
    * Returns all twitch chat messages from now until now - lastMilliseconds
    *
    * @param lastMilliseconds The time span to receive messages
    * @return A list of ChatMessages
    */
  override private[backend] def getMessages(lastMilliseconds: Long) = {
    val currentTime = Calendar.getInstance.getTimeInMillis

    messages.filter(_.timestamp > currentTime - lastMilliseconds).toList
  }
}

/**
  * Provides utility methods for the [[TwitchChat]]-Class.
  */
object TwitchChat {
  /**
    * Creates a new Twitch-Chat object with the given parameters. See [[TwitchChat]] for more information.
    */
  def apply(channelName: String): TwitchChat = new TwitchChat(channelName)
}