package other
import java.io._
import java.nio.file._

object IOTest {
 def main(args: Array[String]): Unit = {
    val in = new File("/tmp/test.jpeg")
    val out = new File("/tmp/out.jpeg")

    val len = in.length
    println(len)

    val os: OutputStream = new FileOutputStream(out)

    val image = Files.readAllBytes(in.toPath)
    os.write(image)
    os.flush
    os.close
 }
}

