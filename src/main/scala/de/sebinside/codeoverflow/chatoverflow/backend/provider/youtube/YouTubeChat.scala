package de.sebinside.codeoverflow.chatoverflow.backend.provider.youtube

import java.util.Calendar

import com.google.api.services.youtube.model.LiveChatMessage
import de.sebinside.codeoverflow.chatoverflow.ChatMessage
import de.sebinside.codeoverflow.chatoverflow.ChatMessage._
import de.sebinside.codeoverflow.chatoverflow.backend.provider.MessageProvider

import scala.collection.JavaConversions._
import scala.collection.immutable

/**
  * Created by renx on 03.12.16.
  */
class YouTubeChat(broadCastID: String) extends MessageProvider {

  val orderMsgByTimeAndId: Ordering[LiveChatMessage] = Ordering[(Long, String)].on[LiveChatMessage](msg => (msg.getSnippet.getPublishedAt.getValue, msg.getId))
  private val liveChatID = YouTubeApiUtils.getLiveChatID(broadCastID) match {
    case Some(id) => id
    case None => throw new IllegalArgumentException("Invalid broadcast ID!")
  }
  private val t = new java.util.Timer
  private val task = new java.util.TimerTask {
    override def run(): Unit = {
      for (item: LiveChatMessage <- YouTubeApiUtils.getLiveChatMessages(liveChatID)) {
        messages = messages + item
      }
    }
  }
  private var messages = immutable.SortedSet[LiveChatMessage]()(orderMsgByTimeAndId)

  startPullingMessages()

  private[backend] def startPullingMessages(interval: Long = 1000L, delay: Long = 0L): Unit = t.schedule(task, delay, interval)

  private[backend] def stopPullingMessages: Boolean = task.cancel

  override private[backend] def getMessages = ???

  override private[backend] def getMessages(lastMilliseconds: Long): List[ChatMessage] = {
    val currentTime = Calendar.getInstance.getTimeInMillis

    var lastTime = currentTime

    if (messages.nonEmpty) {
      lastTime = messages.toList.last.getSnippet.getPublishedAt.getValue
    }

    // FIXME: Test this or find a better method
    messages.filter(m => m.getSnippet.getPublishedAt.getValue > lastTime - lastMilliseconds).toList
  }
}

object YouTubeChat {

  def apply(broadCastID: String): YouTubeChat = new YouTubeChat(broadCastID)

}
