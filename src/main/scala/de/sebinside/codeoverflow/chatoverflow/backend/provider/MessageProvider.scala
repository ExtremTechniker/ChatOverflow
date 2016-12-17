package de.sebinside.codeoverflow.chatoverflow.backend.provider

import de.sebinside.codeoverflow.chatoverflow.ChatMessage

/**
  * Created by sebastian on 04.12.2016.
  */
trait MessageProvider {

  private[backend] def getMessages: List[ChatMessage]

  private[backend] def getMessages(lastMilliseconds: Long): List[ChatMessage]

}
