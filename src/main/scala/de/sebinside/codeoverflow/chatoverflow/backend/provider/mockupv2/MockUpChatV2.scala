package de.sebinside.codeoverflow.chatoverflow.backend.provider.mockupv2

import java.io.File
import java.util.Calendar

import de.sebinside.codeoverflow.chatoverflow.ChatMessage
import de.sebinside.codeoverflow.chatoverflow.backend.provider.MessageProvider
import org.apache.log4j.Logger

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * A MockUpChat is a simulation of a live chat like youtube or twitch.
  * The input is specified in a mockup chat file.
  *
  * @param fileName     the (relative) file name to a chat file to parse and simulate
  * @param defaultDelay the default delay between to messages
  */
class MockUpChatV2(fileName: String, defaultDelay: Int = 100) extends MessageProvider {

  private val logger = Logger.getLogger(this.getClass)

  // Parse text file and create messages
  private val parser = new ChatMessageParser()
  private val elements: List[MockupElement] = parser.parseMockUpFile(Source.fromFile(s"${MockUpChatV2.MOCKUP_FOLDER}/$fileName").getLines())

  private val messages: List[ChatMessage] = createMessageList(elements)
  private var time: Long = 0

  // -------------- CONSTRUCTOR END

  /**
    * Returns all chat messages from the input mockup chat file.
    *
    * @return A full list of chat messages
    */
  override private[backend] def getMessages: List[ChatMessage] = messages

  /**
    * Returns all messages of the last milliseconds. There might be more messages, simulated in the future.
    *
    * @param lastMilliseconds The time span to receive messages
    * @return a list of chat messages
    */
  override private[backend] def getLastMessages(lastMilliseconds: Long): List[ChatMessage] = {

    val now = Calendar.getInstance().getTimeInMillis

    messages.filter(msg => msg.timestamp > now - lastMilliseconds && msg.timestamp < now)
  }

  /**
    * This function is internally used to generate all chat messages form the mockup elements form the parser output.
    *
    * @param elements all mockup elements from the mockup file parser
    * @return A list of chat messages, like in a real world chat
    */
  private def createMessageList(elements: List[MockupElement]): List[ChatMessage] = {

    time = Calendar.getInstance().getTimeInMillis
    logger.info(s"Started MockupChat Construction with timestamp: $time")

    logger.info(s"Started Message Creation with %d elements: %s".format(elements.size, elements.mkString(", ")))

    val messageList = ListBuffer[ChatMessage]()
    var newElements = List[ChatMessage]()
    var currentIndex = 0

    do {

      logger.info(s"New iteration from index $currentIndex.")

      val returnValue = createMessagesUntilEmptyElement(elements.toArray, currentIndex)
      newElements = returnValue._1
      currentIndex = returnValue._2
      messageList ++= newElements

      // FIXME: There might be cases where this ends up in while(true). Should be tested!
    } while (currentIndex < elements.size)

    logger.info("Finally returning %d messages.".format(messageList.size))
    messageList.toList
  }

  /**
    * This function is internally used to generate chat messages until a new line ([[EmptyElement]]) appears.
    * This function is also used to generate all messages of a loop in a recursion.
    *
    * @param allElements All elements, originally parsed from the mockup file parser
    * @param startIndex  the start index. this might be 0 for the first iteration or a higher value if there was a
    *                    new line ([[EmptyElement]] in between). Also important to re-add all messages of a loop to
    *                    the final list.
    * @return A list of chat elements and the index of the next empty element or end of array
    */
  private def createMessagesUntilEmptyElement(allElements: Array[MockupElement], startIndex: Int): (List[ChatMessage], Int) = {

    val messageList = ListBuffer[ChatMessage]()

    var isEmptyElement = false

    var index = startIndex

    // Read until the end of the parsed elements or until a empty element (empty line) appears
    while (index < allElements.length && !isEmptyElement) {

      allElements(index) match {
        case ChatElement(user, msg, isPremium) =>
          logger.info(s"Read ChatElement($user,$msg,$isPremium).")
          messageList += new ChatMessage(msg, user, time, isPremium)
          time += defaultDelay
        case DelayElement(delay) =>
          logger.info(s"Read DelayElement($delay).")
          time += delay
        case RepeatElement(times) =>
          logger.info(s"Read RepeatElement($times).")
          for (i <- 0 until (times - 1)) {
            logger.info(s"Recursion $i.")
            messageList ++= createMessagesUntilEmptyElement(allElements, index + 1)._1
          }
        case EmptyElement() =>
          logger.info(s"Read EmptyElement().")
          isEmptyElement = true
      }
      index += 1

    }

    logger.info(s"Returning %d messages.".format(messageList.size))
    (messageList.toList, index)
  }

}

/**
  * Provides utility methods for the [[MockUpChatV2]]-Class.
  */
object MockUpChatV2 {
  /**
    * The relative path to the mockup chat file folder in the project resources.
    */
  val MOCKUP_FOLDER: String = "src/main/resources/mockup"

  /**
    * Creates a new MockUpChat-Chat object with the given parameters. See [[MockUpChatV2]] for more information.
    */
  def apply(fileName: String): MockUpChatV2 = new MockUpChatV2(fileName)

  /**
    * Creates a new MockUpChat-Chat object with the given parameters. See [[MockUpChatV2]] for more information.
    */
  def apply(fileName: String, defaultDelay: Int): MockUpChatV2 = new MockUpChatV2(fileName, defaultDelay)

  /**
    * Returns a file located in the mockup chat folder. Can be used to prove the existance of an input file.
    *
    * @param fileName the filename of the file
    * @return A file object, created from the given path. Note that this file might not exist
    */
  def getMockUpFile(fileName: String): File = new File("%s/%s".format(MOCKUP_FOLDER, fileName))

}
