package de.sebinside.codeoverflow.chatoverflow

import com.google.api.services.youtube.model.LiveChatMessage

/**
  * A ChatMessages provides information about an entry in a live chat.
  *
  * @param message   The message of the chat entry
  * @param userName  The name of the user who has send the message
  * @param timestamp The timestamp, when the message has been send
  * @param isPremium True, if the user has purchased premium features
  * @param color     The color of the user (Only available in twitch chats)
  */
class ChatMessage(val message: String,
                  val userName: String,
                  val timestamp: Long,
                  val isPremium: Boolean = false,
                  val color: Option[String] = None) {

  override def toString: String = s"ChatMessage($message, $userName, $timestamp, $isPremium, $color)"

}

/**
  * Proviedes utility methods for the [[ChatMessage]]-Class.
  */
object ChatMessage {

  /**
    * Implicitly converts a LiveChatMessage (YouTube) to a generic ChatMessage.
    *
    * @param message The YouTube-[[LiveChatMessage]]
    * @return A [[ChatMessage]]
    */
  implicit def youTubeChatMessage2ChatMessage(message: LiveChatMessage): ChatMessage = {
    new ChatMessage(message.getSnippet.getDisplayMessage,
      message.getAuthorDetails.getDisplayName,
      message.getSnippet.getPublishedAt.getValue,
      message.getAuthorDetails.getIsChatSponsor)
  }

  /**
    * Implicitly converts a List of LiveChatMessages (YouTube) to a list of generic ChatMessages.
    *
    * @param messages A List of YouTube-[[LiveChatMessage]]
    * @return A list of [[ChatMessage]]
    */
  implicit def youTubeChatMessageList2ChatMessageList(messages: List[LiveChatMessage]): List[ChatMessage] = {
    for (msg: LiveChatMessage <- messages) yield youTubeChatMessage2ChatMessage(msg)

  }

}