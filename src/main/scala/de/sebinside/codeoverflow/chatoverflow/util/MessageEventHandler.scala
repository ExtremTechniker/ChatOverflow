package de.sebinside.codeoverflow.chatoverflow.util

import java.awt.{MouseInfo, Robot}
import java.io.File

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation
import org.apache.log4j.Logger
import scala.util.control.Breaks._

import scala.xml.{NodeSeq, XML}

/**
  * Created by renx on 20.12.16.
  */
class MessageEventHandler(settingsXmlPath: String) {

  private var interval: Int = 0
  private val messageEvents : List[MessageEvent] = List[MessageEvent]()
  parseSettingsXml()

  private def parseSettingsXml(): Unit = {
    val settingsNode: NodeSeq = XML.loadFile(new File(settingsXmlPath))

    if (settingsNode.isEmpty)
      throw new Exception("Invalid XML Format of " + settingsXmlPath)

    interval = (settingsNode \ "@interval").text.toInt

    for (eventNode <- settingsNode) {
      eventNode.label match {
        case "event" => {
          val message: String = (eventNode \ "@message").text
          val eventTime: Int = if ((eventNode \ "@once").text.toBoolean) 0 else interval
          (eventNode \ "@type").text match {
            case "key" => {
              val modifierKeys: List[Int] = List[Int]()
              for (keyNode <- eventNode) {
                keyNode match {
                  case <modifierKey>{keyValue}</modifierKey> => modifierKeys :+ keyValue
                }
              }
              messageEvents :+ new KeyEvent(message, eventTime, (eventNode \ "@key").text.toInt, modifierKeys)
            }
            case "mouse" => messageEvents :+ new MouseEvent(message, eventTime, (eventNode \ "@xOffset").text.toInt, (eventNode \ "@yOffset").text.toInt)
            case other => Logger.getLogger(this.getClass).warn(other + " is not a valid type for events")
          }
        }
        case other => Logger.getLogger(this.getClass).warn(other + " is not a valid xml element in settings")
      }
    }
  }

  private[project] def startMessageEventHandler(evaluation: ChatEvaluation): Unit = {
    while (true) {
      val histogram = evaluation.getWordHistogram(interval, _.distinct)
      var found = false
      breakable {
        for (valuePair: (String, Int) <- histogram) {
          messageEvents.foreach(event => if (valuePair._1.matches(event.message)) {
            new Thread(event).start()
            found = true
          })
          if (found) break
        }
      }
      Thread.sleep(interval)
    }
  }
}

private[util] abstract class MessageEvent(val message: String, timeInMillis: Int) extends Runnable {
  private[util] def run
}

private[util] object MessageEvent {
  protected[util] val robot: Robot = new Robot
}

private[util] class MouseEvent(message: String, timeInMillis: Int, xPositionOffset: Int, yPositionOffset: Int) extends MessageEvent(message, timeInMillis) {
  private val fps = 30

  override def run {
    for (i <- 0 to timeInMillis by fps) {
      val mousePoint = MouseInfo.getPointerInfo.getLocation
      val x: Double = mousePoint.getX
      val y: Double = mousePoint.getY
      MessageEvent.robot.mouseMove((x + fps * xPositionOffset.toDouble / timeInMillis.toDouble).toInt, (y + fps * yPositionOffset.toDouble / timeInMillis.toDouble).toInt)
      Thread.sleep(fps)
    }
  }
}

private[util] class KeyEvent(message: String, timeInMillis: Int, key: Int, modifierKeys: List[Int]) extends MessageEvent(message, timeInMillis) {
  override def run = {
    modifierKeys.foreach(modifierKey => MessageEvent.robot.keyPress(modifierKey))
    MessageEvent.robot.keyPress(key)
    Thread.sleep(timeInMillis)
    MessageEvent.robot.keyRelease(key)
    modifierKeys.foreach(modifierKey => MessageEvent.robot.keyRelease(modifierKey))
  }
}
