package de.sebinside.codeoverflow.chatoverflow.project.olacolorcontroll

import com.decodified.scalassh.SshClient
import de.sebinside.codeoverflow.chatoverflow.ChatMessage
import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation

import scala.collection.mutable.ListBuffer

/**
  * Created by sebastian on 13.12.2016.
  */
private[project] class CommunityColor extends OlaColorControl {
  override protected def evaluate(evaluation: ChatEvaluation, client: SshClient) = {

    val INTERVAL = 5000

    while (true) {

      val messages: List[ChatMessage] = evaluation.getMessages(INTERVAL)

      val words = for (message: ChatMessage <- messages) yield message.message

      val colors = ListBuffer[Int]()

      for (word: String <- words) {
        if (word forall Character.isDigit) {
          val num = word.toInt

          if (num >= 0 && num <= 100) {

            colors += num

          }
        }
      }

      if (colors.size < 3) {
        println("Too less numbers!")
      } else {

        setColor(client, 1, (255,
          (colors.head * 2.55).toInt,
          (colors(1) * 2.55).toInt,
          (colors(2) * 2.55).toInt))

      }

      Thread.sleep(INTERVAL)
    }
  }

  override private[project] def getName = "CommunityColor"

  override private[project] def getDescription = "Changes the room color to the color from the chat."
}

object CommunityColor {
  def apply(): CommunityColor = new CommunityColor()
}
