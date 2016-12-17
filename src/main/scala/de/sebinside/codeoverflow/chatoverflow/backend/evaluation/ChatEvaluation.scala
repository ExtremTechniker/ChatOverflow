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

  def getWordHistogram(lastMilliseconds: Long, predicate: Seq[String] => Seq[String] = identity): List[(String, Int)] = {

    // println("Filtered: " + getMessages(lastMilliseconds).size)

    getMessages(lastMilliseconds) //all messages of last n milliseconds
      .map(_.message) //extract text from messages
      .flatMap(message => {
        val cleanedMessage = message.split("\\s+").map(_.toLowerCase.replaceAll("\\W", ""))
        predicate(cleanedMessage)
      }) //split to single words
      .groupBy(identity)
      .mapValues(_.size) //count number of occurences of every word
      .toList
      .sortBy(_._2)
      .reverse
  }

  /**
    * @return all messages sent during the last n milliseconds as provided by the YouTubeMessageProvider
    */
  def getMessages(lastMilliseconds: Long): List[ChatMessage] = {
    _messageProvider.getMessages(lastMilliseconds)
  }
}

object ChatEvaluation {

  def apply(messageProvider: MessageProvider): ChatEvaluation = new ChatEvaluation(messageProvider)

}