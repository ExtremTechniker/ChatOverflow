package de.sebinside.codeoverflow.chatoverflow.project.messageevent

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation
import de.sebinside.codeoverflow.chatoverflow.project.ChatProject
import de.sebinside.codeoverflow.chatoverflow.util.MessageEventHandler

/**
  * Created by renx on 20.12.16.
  */

/**
  * Created by renx on 13.12.16.
  */
private[messageevent] class MessageEvent extends ChatProject {

  private val intervall = 2000

  override private[project] def getName: String = "MessageEvent"

  override private[project] def getDescription: String = "Führt in XML-definierte Events aus"

  override private[project] def start(evaluation: ChatEvaluation) = {
    val handler : MessageEventHandler = new MessageEventHandler("")
    handler.startMessageEventHandler(evaluation)
  }
}

object MessageEvent {

  def apply(): MessageEvent = new MessageEvent()

}