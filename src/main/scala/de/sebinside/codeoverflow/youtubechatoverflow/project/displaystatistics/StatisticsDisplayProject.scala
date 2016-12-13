package de.sebinside.codeoverflow.youtubechatoverflow.project.displaystatistics

import com.google.api.services.youtube.model.LiveChatMessage
import de.sebinside.codeoverflow.youtubechatoverflow.backend.evaluation.ChatEvaluation
import de.sebinside.codeoverflow.youtubechatoverflow.project.ChatProject

/**
  * Created by seb on 13.12.2016.
  */
private[displaystatistics] class StatisticsDisplayProject extends ChatProject {

  //private val _window = new StatisticsDisplay()

  override private[project] def getName: String = "StatisticsDisplayProject"

  override private[project] def getDescription: String = "Project displaying various statistics about chat messages"

  override private[project] def start(evaluation: ChatEvaluation) = {
    //_window.openDisplay()

  }
}

object StatisticsDisplayProject {

  def apply(): StatisticsDisplayProject = new StatisticsDisplayProject

}
