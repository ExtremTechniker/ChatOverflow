package de.sebinside.codeoverflow.chatoverflow.project._sampleproject

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation
import de.sebinside.codeoverflow.chatoverflow.project.ChatProject

/**
  * This is a sample project class. It extends the ChatProject Trait.
  * The visibility is package private, only the companion object should instantiate the class.
  * To work properly, the project has to be registered in [[de.sebinside.codeoverflow.chatoverflow.ChatOverflow.initProjects]].
  */
private[_sampleproject] class SampleProject extends ChatProject {
  override private[project] def getName = "Sample project"

  override private[project] def getDescription = "Sample description"

  override private[project] def start(evaluation: ChatEvaluation, arguments: Map[String, String]) = println("I'm special.")
}

/**
  * This is the companion object of the sample project class.
  * It provides the public apply method to register the chat project at start.
  */
object SampleProject {
  def apply: SampleProject = new SampleProject()
}