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

      val messages: List[ChatMessage] = evaluation.getMessages(10000)

      for (message <- messages) {
        println("%s: %s".
          format(message.userName, message.message))
      }

      Thread.sleep(1000)

    }
  }
}

object DummyProject {

  def apply(): DummyProject = new DummyProject()

}