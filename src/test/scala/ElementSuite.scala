/**
  * Created by yuki on 2016/09/25.
  */
import org.scalatest.Suite
import Element.elem
class ElementSuite extends Suite{
 def testUniformElement(): Unit = {
   val ele = elem('x',2,3)
   assert(ele.width == 2)
   assert(ele.height == 3)
 }
}
