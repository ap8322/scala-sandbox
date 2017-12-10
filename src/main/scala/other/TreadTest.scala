object TreadTest {
  def main(args: Array[String]) {
    new Thread() {
      override def run() = {
        for (i <- 1 to 10) {
          Thread.sleep(1000)
          println("12345")
        }
      }
    }.start()

    Thread.sleep(5000)
    println("67890")
  }
}
