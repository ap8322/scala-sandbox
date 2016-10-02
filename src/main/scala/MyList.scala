/**
  * Created by yuki on 2016/09/29.
  */
  sealed trait MyList[A]
  case class MyCons[A](head: A, tail: MyList[A]) extends MyList[A]
  object MyNil extends MyList[A]

  list match {
    case MyCons(h, t) => ???
    case MyNil => ???
  }

List[]

