package de.sebinside.codeoverflow.chatoverflow.backend.provider.mockup

import java.io.File
import java.util.Calendar

import com.google.api.services.youtube.model.LiveChatMessage
import de.sebinside.codeoverflow.chatoverflow.ChatMessage
import de.sebinside.codeoverflow.chatoverflow.backend.provider.MessageProvider

import scala.io.Source

/**
  * Created by renx on 05.12.16.
  */
class MockUpChat(fileName: String) extends MessageProvider {
  private val messages: List[LiveChatMessage] =
    LiveChatMessageParser(Source.fromFile("%s/%s".format(MockUpChat.MOCKUP_FOLDER, fileName)).mkString)

  override private[backend] def getMessages: List[ChatMessage] = messages

  override private[backend] def getMessages(lastMilliseconds: Long): List[ChatMessage] = {
    val currentTime = Calendar.getInstance.getTimeInMillis
    messages.filter(m => currentTime > m.getSnippet.getPublishedAt.getValue &&
      m.getSnippet.getPublishedAt.getValue > currentTime - lastMilliseconds)
  }

}

object MockUpChat {
  val MOCKUP_FOLDER: String = "src/main/resources/mockup"

  def apply(fileName: String): MockUpChat = new MockUpChat(fileName)

  def getMockUpFile(fileName: String): File = new File("%s/%s".format(MOCKUP_FOLDER, fileName))

}
