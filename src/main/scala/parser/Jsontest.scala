import java.io.FileReader
import com.test.scala._

object Jsontest extends Json {
  def main(args: Array[String]) {
    val reader = new FileReader(args(0))
    println(parseAll(value,reader))
  }
}
