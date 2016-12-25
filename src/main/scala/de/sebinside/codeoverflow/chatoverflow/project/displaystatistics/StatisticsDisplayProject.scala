package de.sebinside.codeoverflow.chatoverflow.project.displaystatistics

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation
import de.sebinside.codeoverflow.chatoverflow.project.ChatProject

/**
  * Created by seb on 13.12.2016.
  */
class StatisticsDisplayProject extends ChatProject {

  private val _window = new StatisticsDisplay()

  override private[project] def getName: String = "StatisticsDisplayProject"

  override private[project] def getDescription: String = "Project displaying various statistics about chat messages"

  override private[project] def start(evaluation: ChatEvaluation, arguments: Map[String, String]) = {
    _window.openDisplay()
    while (true) {
      Thread.sleep(2000)
      val list = evaluation.getWordHistogram(10000).take(5)
      _window.updateHistogram(list)
    }

  }
}

object StatisticsDisplayProject {

  def apply(): StatisticsDisplayProject = new StatisticsDisplayProject

}