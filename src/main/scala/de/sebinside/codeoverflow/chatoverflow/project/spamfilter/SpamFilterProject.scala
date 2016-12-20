package de.sebinside.codeoverflow.chatoverflow.project.spamfilter

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation
import de.sebinside.codeoverflow.chatoverflow.project.ChatProject

/**
  * Created by Dennis on 20.12.2016.
  */
class SpamFilterProject extends ChatProject {

  private val messageMinLength = 3

  override private[project] def getName = "SpamFilterProject"

  override private[project] def getDescription = "Removes frequently spammed messages from chat and displays " +
    "unfrequent (= interesting) messages in a separate window"


  override private[project] def start(evaluation: ChatEvaluation): Unit = {
    Thread.sleep(1000)
    while (true) {
      val messages = evaluation.getUncommonMessages(1000, 10000)
      for (message <- messages.filter(message => message.split("\\s+").size >= messageMinLength)) {
        println(message)
      }
      Thread.sleep(1000)
    }
  }


}

object SpamFilterProject {
  def apply(): SpamFilterProject = new SpamFilterProject
}
