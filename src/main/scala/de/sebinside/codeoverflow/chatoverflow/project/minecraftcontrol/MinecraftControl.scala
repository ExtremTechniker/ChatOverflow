package de.sebinside.codeoverflow.chatoverflow.project.minecraftcontrol

import java.awt.event.KeyEvent
import java.awt.{MouseInfo, Robot}

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation
import de.sebinside.codeoverflow.chatoverflow.project.ChatProject

/**
  * Created by renx on 13.12.16.
  */
private[minecraftcontrol] class MinecraftControl extends ChatProject {

  private val robot = new Robot()
  private val intervall = 2000
  private val pixelMouse = 350

  override private[project] def getName: String = "MinecraftControl"

  override private[project] def getDescription: String = "Kontrolliere Minecraft"

  override private[project] def start(evaluation: ChatEvaluation) = {

    while (true) {

      val histogram = evaluation.getWordHistogram(intervall, _.distinct)

      var found = false
      for (valuePair: (String, Int) <- histogram) {
        if (!found) {
          found = true
          valuePair._1 match {
            //case "up" =>
            //  println("Move up!" + valuePair._2)
            //  pressKey(KeyEvent.VK_SPACE, intervall)
            //case "down" =>
            //  println("Move down!"  + valuePair._2)
            ///  pressKey(KeyEvent.VK_SHIFT, intervall)
            case "left" =>
              println("Turn left!" + valuePair._2)
              moveMouse(-pixelMouse, intervall)
            case "right" =>
              println("Turn right!" + valuePair._2)
              moveMouse(pixelMouse, intervall)
            case "go" =>
              println("Move forward!" + valuePair._2)
              pressKey(KeyEvent.VK_W, intervall)
            case "back" =>
              println("Move back!")
              pressKey(KeyEvent.VK_S, intervall)
            case "screenshot" =>
              println("Make Screenshot!")
              pressKey(KeyEvent.VK_F2, 0)
              Thread.sleep(intervall)
            case _ =>
              println("Do nothing!")
              found = false
          }
        }
      }
    }
  }

  private def pressKey(key: Int, time: Int) = {
    try {
      robot.keyPress(key)
      Thread.sleep(time)
      robot.keyRelease(key);
    } catch {
      case _ => println("Exception")
    }
  }

  private def moveMouse(xOffset: Int, time: Int) {
    try {
      for (i <- 0 to time by 30) {
        val mousePoint = MouseInfo.getPointerInfo.getLocation
        val x: Double = mousePoint.getX
        val y: Integer = mousePoint.getY.toInt
        robot.mouseMove((x + 30.0 * xOffset.toDouble / time.toDouble).toInt, y)
        Thread.sleep(30)
      }
    } catch {
      case _ => println("Exception")
    }
  }
}

object MinecraftControl {

  def apply(): MinecraftControl = new MinecraftControl()

}