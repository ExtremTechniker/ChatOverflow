package de.sebinside.codeoverflow.chatoverflow.project.displaystatistics

import scalafx.application.{JFXApp, Platform}
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.{TableColumn, TableView}
import scalafx.scene.paint.Color._


/**
  * Created by dennis on 13.12.2016.
  */


/**
  * A ScalaFX Window with the ability to display colors.
  */
private[displaystatistics] class StatisticsDisplay extends JFXApp {

  val histogram = ObservableBuffer[(String, Int)](("noch keine einträge", 0))

  stage = new JFXApp.PrimaryStage {
    title.value = "Chat Statistics"
    width = 600
    height = 600
    scene = new Scene {
      fill = White
      content = Seq(new TableView[(String, Int)](histogram) {
        padding = Insets(20)
        columns ++= List(
          new TableColumn[(String, Int), String] {
            text = "word"
            cellValueFactory = { elem => StringProperty(elem.value._1) }
            prefWidth = 200
          },
          new TableColumn[(String, Int), String] {
            text = "count"
            cellValueFactory = { elem => StringProperty(elem.value._2.toString) }
            prefWidth = 200
          }
        )
      }
      )
    }
  }


  private[displaystatistics] def openDisplay(): Unit = {
    new Thread(new Runnable {
      override def run(): Unit = main(null)
    }).start()
  }

  private[displaystatistics] def updateHistogram(hist: List[(String, Int)]): Unit = {

    if (hist != null) {
      Platform.runLater(new Runnable {
        override def run(): Unit = {
          histogram.clear()
          for (pair <- hist) {
            histogram += pair
          }
        }
      })
    }
  }


}

