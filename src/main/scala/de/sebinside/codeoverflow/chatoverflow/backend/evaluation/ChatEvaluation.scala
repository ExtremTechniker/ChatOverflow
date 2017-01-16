package de.sebinside.codeoverflow.chatoverflow.backend.evaluation

import de.sebinside.codeoverflow.chatoverflow.ChatMessage
import de.sebinside.codeoverflow.chatoverflow.backend.provider.MessageProvider

/**
  * Created by seb on 29.11.2016.
  */
class ChatEvaluation(_messageProvider: MessageProvider) {

  /**
    * @return all messages as provided by the YouTubeMessageProvider
    */
  def getMessages: List[ChatMessage] = {
    _messageProvider.getMessages
  }

  def getUncommonMessages(lastMilliseconds: Long, comparationTime: Long): List[String] = {
    val messages = getMessages(lastMilliseconds).map(_.message)
    val histogram = getWordHistogram(comparationTime).toMap
    if (messages.nonEmpty) {
      val messageScores = messages.map(message => {
        val cleanedMessage = cleanMessage(message)
        val score = cleanedMessage.map(word => histogram(word)).sum / cleanedMessage.size
        (message, score)
      })
      val numberOfMessages: Float = messages.size
      val averageScore = messageScores.map(_._2).sum / numberOfMessages
      val minScore = messageScores.map(_._2).min
      val maxScore = messageScores.map(_._2).max
      val variance = messageScores.map(item => (item._2 - averageScore) * (item._2 - averageScore)).sum / (numberOfMessages - 1)
      val standardDeviation = math.sqrt(variance)
      messageScores.filter(item => item._2 < averageScore - math.sqrt(numberOfMessages) / numberOfMessages * standardDeviation && item._2 < minScore + 0.2 * (maxScore - minScore)).map(_._1).distinct
    } else {
      List.empty
    }
  }

  private[ChatEvaluation] def cleanMessage(message: String): Seq[String] = {
    message.split("\\s+").map(_.toLowerCase.replaceAll("[^a-zäöü]", ""))
  }

  def getWordHistogram(lastMilliseconds: Long, predicate: Seq[String] => Seq[String] = identity): List[(String, Int)] = {
    // println("Filtered: " + getMessages(lastMilliseconds).size)
    computeWordHistogram(getMessages(lastMilliseconds).map(_.message)) //extract text from messages
      .sortBy(_._2)
      .reverse
  }

  def computeWordHistogram(messages: List[String], predicate: Seq[String] => Seq[String] = identity): List[(String, Int)] = {
    messages
      .flatMap(message => predicate(cleanMessage(message))) //split to single words
      .groupBy(identity)
      .mapValues(_.size) //count number of occurences of every word
      .toList
  }

  /**
    * @return all messages sent during the last n milliseconds as provided by the YouTubeMessageProvider
    */
  def getMessages(lastMilliseconds: Long): List[ChatMessage] = {
    _messageProvider.getLastMessages(lastMilliseconds)
  }
}

object ChatEvaluation {

  def apply(messageProvider: MessageProvider): ChatEvaluation = new ChatEvaluation(messageProvider)

}