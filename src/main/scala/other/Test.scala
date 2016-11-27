//object Test {
//  def main(args: Array[String]) {
//    val sc = new java.util.Scanner(System.in)
//    val n = sc.nextInt
//    val list = List.fill(n,6)(sc.next)
//    val a = list.collect {
//      case h :: t if h == 0 => (t.sum,t(1)+t(2))
//      case h :: t if h == 1 => (t.sum,t(3)+t(4))
//      case _ => throw new IllegalArgumentException()
//    }
//
//    println(a.filter{ case (s1,s2) => s1 > 360 && s2 > 160}.size)
//  }
//}
