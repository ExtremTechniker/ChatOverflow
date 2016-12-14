package de.sebinside.codeoverflow.youtubechatoverflow.backend.provider.mockupInternal

/**
  * Created by seb on 13.12.16.
  */
class Chat {

  def simulate(messages: Seq[ChatMessage]): Unit = {
    for (m <- messages)
      println("%s sagt %s.".format(m.liveChatMessage.getAuthorDetails.getDisplayName,
        m.liveChatMessage.getSnippet.getDisplayMessage))
  }

  def main(args: Array[String]): Unit = {
    /*
        simulate { Seq(
          "Ich bin der Ano!",
          "skate" ! "ich bin sponsor".*,
          "andre" * "ich bin auch sponsor!"
        )}
    */
  }

}
