package de.sebinside.codeoverflow.chatoverflow.project.minecraftcontrol

import java.awt.event.KeyEvent
import java.awt.{MouseInfo, Robot}

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation
import de.sebinside.codeoverflow.chatoverflow.project.ChatProject
import de.sebinside.codeoverflow.chatoverflow.util.MessageEventHandler

/**
  * Created by renx on 13.12.16.
  */
private[minecraftcontrol] class MinecraftControl extends ChatProject {

  private val robot = new Robot()
  private val intervall = 2000
  private val pixelMouse = 350

  override private[project] def getName: String = "MinecraftControl"

  override private[project] def getDescription: String = "Kontrolliere Minecraft"

  override private[project] def start(evaluation: ChatEvaluation, arguments: Map[String, String]) = {
    val handler = new MessageEventHandler("src/main/resources/MinecraftSettings.xml")
    handler.startMessageEventHandler(evaluation)
  }
}

object MinecraftControl {

  def apply(): MinecraftControl = new MinecraftControl()

}