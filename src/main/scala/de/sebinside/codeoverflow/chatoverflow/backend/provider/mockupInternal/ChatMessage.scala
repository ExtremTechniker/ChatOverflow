package de.sebinside.codeoverflow.chatoverflow.backend.provider.mockupInternal

import com.google.api.services.youtube.model.{LiveChatMessage, LiveChatMessageAuthorDetails, LiveChatMessageSnippet}

/**
  * Created by seb on 13.12.16.
  */
@Deprecated
class ChatMessage {

  val liveChatMessage: LiveChatMessage = new LiveChatMessage().
    setAuthorDetails(new LiveChatMessageAuthorDetails()).
    setSnippet(new LiveChatMessageSnippet)

  def this(name: String, message: String) = {
    this()
    liveChatMessage.getAuthorDetails.setDisplayName(name)
    liveChatMessage.getSnippet.setDisplayMessage(message)
  }

  def *(): ChatMessage = {
    liveChatMessage.getAuthorDetails.setIsChatSponsor(true)
    this
  }


}

@Deprecated
object ChatMessage {

  implicit def string2message(message: String): ChatMessage = {
    new ChatMessage("anonymn702", message)
  }

  implicit def string2evenRicherString(s: String): EvenRicherString = {
    new EvenRicherString(s)
  }


}

@Deprecated
class EvenRicherString(val s: String) {

  def !(message: String): ChatMessage = {
    new ChatMessage(s, message)
  }

  def !(message: ChatMessage) = {
    message.liveChatMessage.getAuthorDetails.setDisplayName(s)
    message
  }

  def *(message: String): ChatMessage = {
    val msg = new ChatMessage(s, message)
    msg.*()
  }

}