package de.sebinside.codeoverflow.chatoverflow.project.displaystatistics

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

    if (hist != null) {Platform.runLater( new Runnable {
      override def run() = {
        histogram.clear()
        for(pair <- hist) {
          histogram += pair
        }
      }
    })}
  }


}

