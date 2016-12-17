package de.sebinside.codeoverflow.chatoverflow.project.pizza

import de.sebinside.codeoverflow.chatoverflow.backend.evaluation.ChatEvaluation
import de.sebinside.codeoverflow.chatoverflow.project.ChatProject

/**
  * Created by sebastian on 13.12.2016.
  */
private[project] class WhatPizzaProject extends ChatProject {

  val INTERVAL = 5000

  override private[project] def getName = "WhatPizza"

  override private[project] def getDescription = "Chooses a tasty pizza, just for you!"

  override private[project] def start(evaluation: ChatEvaluation) = {

    while (true) {

      val words = evaluation.getWordHistogram(INTERVAL)

      for ((word, quantity) <- words) {

        if (quantity > 5) {
          println("%s - %d".format(word, quantity))
        }

      }

      println("-------------------------------------")

      Thread.sleep(INTERVAL)

    }

  }
}

object WhatPizzaProject {
  def apply(): WhatPizzaProject = new WhatPizzaProject()
}