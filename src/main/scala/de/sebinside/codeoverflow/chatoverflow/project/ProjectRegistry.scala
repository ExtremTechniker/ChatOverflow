package de.sebinside.codeoverflow.chatoverflow.project

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation

import scala.collection.mutable

/**
  * Created by seb on 29.11.2016.
  */
object ProjectRegistry {

  private val projects = mutable.Map[String, ChatProject]()

  def clear(): Unit = projects.clear()

  def registerAll(projects: Seq[ChatProject]): Unit = projects.foreach(f => register(f))

  def register(project: ChatProject): Unit = projects += project.getName.toUpperCase -> project

  def exists(projectName: String): Boolean = projects.contains(projectName.toUpperCase)

  def start(projectName: String, chatEvaluation: ChatEvaluation, arguments: Map[String, String]): Unit =
    projects(projectName.toUpperCase).start(chatEvaluation, arguments)

  def listProjects: Seq[String] = (for ((_, project) <- projects) yield project.getName).toSeq
}
