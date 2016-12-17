package de.sebinside.codeoverflow.chatoverflow

import com.google.api.services.youtube.model.LiveChatMessage

/**
  * Created by seb on 17.12.16.
  */
class ChatMessage(val message: String, val userName: String, val isPremium: Boolean, val timestamp: Long) {

}

object ChatMessage {

  // TODO: Currently supports only message, user, isPremium (VIP/Sub) and message timestamp
  implicit def youTubeChatMessage2ChatMessage(message: LiveChatMessage): ChatMessage = {
    new ChatMessage(message.getSnippet.getDisplayMessage,
      message.getAuthorDetails.getDisplayName,
      message.getAuthorDetails.getIsChatSponsor,
      message.getSnippet.getPublishedAt.getValue)
  }

  implicit def youTubeChatMessageList2ChatMessageList(messages: List[LiveChatMessage]): List[ChatMessage] = {
    for (msg: LiveChatMessage <- messages) yield youTubeChatMessage2ChatMessage(msg)

  }

}