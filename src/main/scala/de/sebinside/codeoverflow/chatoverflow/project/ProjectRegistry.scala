package de.sebinside.codeoverflow.chatoverflow.project

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation

import scala.collection.mutable

/**
  * The project registry is used to register chat projects and wrap the project methods.
  * Its also used to start registered projects.
  */
object ProjectRegistry {

  private val projects = mutable.Map[String, ChatProject]()

  /**
    * Clears all previously registered projects from the list.
    */
  def clear(): Unit = projects.clear()

  /**
    * Registers all given chat projects.
    *
    * @param projects A sequence of chat projects
    */
  def registerAll(projects: Seq[ChatProject]): Unit = projects.foreach(f => register(f))

  /**
    * Registers a given chat project.
    *
    * @param project A chat project
    */
  def register(project: ChatProject): Unit = projects += project.getName.toUpperCase -> project

  /**
    * Checks if a project with the given project name was previously registered
    *
    * @param projectName the project name to test
    * @return true, if a project with the given name was found
    */
  def exists(projectName: String): Boolean = projects.contains(projectName.toUpperCase)

  /**
    * Starts a registered chat project with the given parameters.
    *
    * @param projectName    The name of the chat project to start. Must be previously registered.
    * @param chatEvaluation The chat evaluation to gather chat messages from
    * @param arguments      optional arguments to control the behavior of the chat project
    */
  def start(projectName: String, chatEvaluation: ChatEvaluation, arguments: Map[String, String]): Unit =
    projects(projectName.toUpperCase).start(chatEvaluation, arguments)

  /**
    * Lists all registered chat projects.
    *
    * @return A sequence with all project names
    */
  def listProjects: Seq[String] = (for ((_, project) <- projects) yield project.getName).toSeq

  /**
    * Returns the real name of a project
    *
    * @param projectName the project name (not case sensitive)
    * @return the project name (case sensitive)
    */
  def getName(projectName: String): String = projects(projectName.toUpperCase()).getName

  /**
    * Returns the description of a project
    *
    * @param projectName the project name
    * @return the description, provided from the author
    */
  def getDescription(projectName: String): String = projects(projectName.toUpperCase).getDescription

  /**
    * Returns a pretty printed list of available arguments, if specified
    *
    * @param projectName the project name
    * @return a string with pretty printed arguments
    */
  def prettyPrintAvailableArguments(projectName: String): String = {

    val args = projects(projectName.toUpperCase()).getAvailableArgumentDescription

    var returnString = ""

    for (arg <- args) {
      returnString += "\"%s\": %s\n".format(arg._1, arg._2)
    }

    returnString
  }
}
