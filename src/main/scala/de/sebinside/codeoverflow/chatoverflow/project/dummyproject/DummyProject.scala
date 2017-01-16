package de.sebinside.codeoverflow.chatoverflow.project.dummyproject

import de.sebinside.codeoverflow.chatoverflow.ChatMessage
import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation
import de.sebinside.codeoverflow.chatoverflow.project.ChatProject

/**
  * Created by seb on 29.11.2016.
  */
private[dummyproject] class DummyProject extends ChatProject {
  override private[project] def getName: String = "DummyProject"

  override private[project] def getDescription: String = "Just a demo project"

  override private[project] def getAvailableArgumentDescription =
    Map[String, String](
      "printAll" -> "True, if all messages should be printed at once.",
      "delay" -> "The delay in milliseconds to wait in between message polling.")

  override private[project] def start(evaluation: ChatEvaluation, arguments: Map[String, String]) = {

    if (arguments.contains("printAll") && arguments("printAll").toUpperCase == "TRUE") {
      for (message <- evaluation.getMessages) {
        printChatMessage(message)
      }

    } else {

      val delay = if (arguments.contains("delay")) arguments("delay").toInt else 1000

      while (true) {

        val messages: List[ChatMessage] = evaluation.getMessages(delay)

        println(s"\n------------------ Messages in the last $delay milliseconds: ------------------")

        for (message <- messages) {

          printChatMessage(message)
        }

        Thread.sleep(delay)

      }
    }
  }

  private def printChatMessage(message: ChatMessage): Unit = {
    val subChar: String = if (message.isPremium) "*" else ""

    val colorString = message.color match {
      case Some(color) => color
      case None => ""
    }

    println("%s%s%s: %s".
      format(subChar, message.userName, colorString, message.message))
  }
}

object DummyProject {

  def apply(): DummyProject = new DummyProject()

}