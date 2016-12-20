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

  override private[project] def start(evaluation: ChatEvaluation) = {

    while (true) {

      val messages: List[ChatMessage] = evaluation.getMessages(1000)

      println("Messages in the last second:")

      for (message <- messages) {

        val subChar: String = if (message.isPremium) "*" else ""

        val colorString = message.color match {
          case Some(color) => color
          case None => ""
        }

        println("%s%s%s: %s".
          format(subChar, message.userName, colorString, message.message))
      }

      Thread.sleep(1000)

    }
  }
}

object DummyProject {

  def apply(): DummyProject = new DummyProject()

}