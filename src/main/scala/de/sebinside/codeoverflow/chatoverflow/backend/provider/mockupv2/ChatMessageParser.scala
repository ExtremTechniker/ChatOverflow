package de.sebinside.codeoverflow.chatoverflow.backend.provider.mockupv2

import org.apache.log4j.Logger

import scala.collection.mutable.ListBuffer
import scala.util.Random
import scala.util.parsing.combinator.JavaTokenParsers

trait MockupElement

/**
  * Created by seb on 25.12.16.
  */
class ChatMessageParser(var defaultDelay: Int = 1000,
                        var defaultEscape: String = "!",
                        var defaultSeparator: String = ":",
                        var defaultPremiumSymbol: String = "*") extends JavaTokenParsers {

  // Logger
  private val logger = Logger.getLogger(this.getClass)

  // Random for random user-number generation
  private val random = new Random()


  // A message only (not starting with the escapeString and not containing a colon)
  private val messageOnly: Parser[ChatElement] = (s"[^$defaultEscape][^$defaultSeparator]*".r | stringLiteral) ^^ {
    message => ChatElement("user%d".format(random.nextInt(1000)), message, isPremium = false)
  }

  // username and message, separated by colon
  private val userAndMessage: Parser[ChatElement] =
    (s"[^$defaultPremiumSymbol$defaultEscape][^$defaultSeparator]*".r | stringLiteral) ~ ":".r ~ ".*".r ^^ {
      case user ~ _ ~ message => ChatElement(user, message, isPremium = false)
    }

  // Premium symbol, username and message
  private val userAndMessagePremium: Parser[ChatElement] = s"$defaultPremiumSymbol" ~ userAndMessage ^^ {
    case _ ~ message => message.copy(isPremium = true)
  }

  // Chat, Delay and Repeat elements
  private val chatElement: Parser[ChatElement] = userAndMessagePremium | userAndMessage | messageOnly
  private val delayElement: Parser[DelayElement] = s"$defaultEscape".r ~> "[\\d]+".r ^^ {
    delay => DelayElement(delay.toInt)
  }
  private val repeatElement: Parser[RepeatElement] = s"$defaultEscape".r ~> "[\\d]+".r <~ "[x]".r ^^ {
    times => RepeatElement(times.toInt)
  }

  private val element: Parser[MockupElement] = chatElement | repeatElement | delayElement

  def parseMockUpFile(lines: Iterator[String]): List[MockupElement] = {

    val elementList = ListBuffer[MockupElement]()

    var counter = 0

    for (line: String <- lines) {

      if (line.isEmpty) {
        logger.info("Parsing... line %d is empty.".format(counter))
        elementList += EmptyElement()
      } else {
        logger.info("Parsing... line %d.".format(counter))
        elementList += parseMockUpLine(line).getOrElse(EmptyElement())
      }

      counter += 1
    }

    elementList.toList
  }

  private def parseMockUpLine(content: String): Option[MockupElement] = parseAll(element, content) match {
    case Success(result, _) =>
      logger.info("Parsed input successfully.")
      Some(result)
    case Error(msg, _) =>
      logger.error("Error while parsing: %s".format(msg))
      None
    case Failure(msg, _) =>
      logger.error("Failure while parsing: %s".format(msg))
      None
  }

  case class ChatElement(user: String, message: String, isPremium: Boolean) extends MockupElement

  case class DelayElement(delay: Int) extends MockupElement

  case class RepeatElement(times: Int) extends MockupElement

  case class EmptyElement() extends MockupElement

}
