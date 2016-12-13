package de.sebinside.codeoverflow.youtubechatoverflow.project.displaystatistics

/**
  * Created by dennis on 13.12.2016.
  */


import scalafx.application.{JFXApp, Platform}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TableColumn, TableView}
import scalafx.scene.control.TableColumn._
import scalafx.Includes._


/**
  * A ScalaFX Window with the ability to display colors.
  */
private[displaystatistics] object StatisticsDisplay extends JFXApp {

  var histogram = ObservableBuffer[(String, Int)](
    ("andre", 5), ("dennis", 10)
  )

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
      }, new Button {
        onAction =  (event: ActionEvent) => {histogram = ObservableBuffer[(String, Int)](
          ("andre", 10), ("dennis", 20), ("sebastian", 2)
        )
        println("button pressed")}
      }
      )
    }
  }


  private[displaystatistics] def openDisplay(): Unit = {
    new Thread(new Runnable {
      override def run(): Unit = main(null)
    }).start()
  }


}
