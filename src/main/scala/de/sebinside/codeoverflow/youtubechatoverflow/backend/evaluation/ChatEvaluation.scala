package de.sebinside.codeoverflow.youtubechatoverflow.backend.evaluation

import com.google.api.services.youtube.model.LiveChatMessage
import de.sebinside.codeoverflow.youtubechatoverflow.backend.provider.YouTubeMessageProvider

/**
  * Created by seb on 29.11.2016.
  */
class ChatEvaluation(_messageProvider: YouTubeMessageProvider) {

  /**
    * @return all messages as provided by the YouTubeMessageProvider
    */
  def getMessages : List[LiveChatMessage] = {
    _messageProvider.getMessages
  }

  def getWordHistogram(lastMilliseconds: Long, predicate: Seq[String] => Seq[String] = identity): List[(String, Int)] = {

    // println("Filtered: " + getMessages(lastMilliseconds).size)

    getMessages(lastMilliseconds) //all messages of last n milliseconds
      .map(_.getSnippet.getDisplayMessage) //extract text from messages
      .flatMap(message => {
        val cleanedMessage = message.split("\\s+").map(_.toLowerCase.replaceAll("\\W", ""))
        predicate(cleanedMessage)
      }) //split to single words
      .groupBy(identity)
      .mapValues(_.size) //count number of occurences of every word
      .toList
      .sortBy(_._2)
  }

  /**
    * @return all messages sent during the last n milliseconds as provided by the YouTubeMessageProvider
    */
  def getMessages(lastMilliseconds: Long): List[LiveChatMessage] = {
    _messageProvider.getMessages(lastMilliseconds)
  }
}

object ChatEvaluation {

  def apply(messageProvider: YouTubeMessageProvider): ChatEvaluation = new ChatEvaluation(messageProvider)

}