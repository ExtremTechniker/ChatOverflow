package de.sebinside.codeoverflow.chatoverflow.project

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation

/**
  * Created by seb on 29.11.2016.
  */
private[project] trait ChatProject {

  private[project] def getName: String

  private[project] def getDescription: String

  private[project] def getAvailableArgumentDescription: Map[String, String] = Map[String, String]()

  private[project] def start(evaluation: ChatEvaluation, arguments: Map[String, String])
}
