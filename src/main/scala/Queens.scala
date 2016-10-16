/**
  * Created by yuki on 2016/10/16.
  */
class Queens {
 def queens(n: Int): List[List[(Int,Int)]] = {
  def placeQueens(k: Int):List[List[(Int,Int)]] = {
   if(k == 0)
    List(List())
    else
     for {
       queens <- placeQueens(k - 1)
       column <- 1 to n
       queen  = (k,column)
       if isSafe(queen,queens)
     } yield queen :: queens
  }
   placeQueens(n)
 }

  def isSafe(queen: (Int,Int),queens: List[(Int,Int)]): Boolean = {
    queens.forall(q => !inCheck(queen,q))
  }



  def inCheck(q1: (Int,Int),q2: (Int,Int)): Boolean = {
    q1._1 == q2._1 || // 同じ段
    q1._2 == q2._2 || // 同じ行
   (q1._1 - q2._1).abs == (q1._2 - q2._2).abs // 同じ筋
  }
}
