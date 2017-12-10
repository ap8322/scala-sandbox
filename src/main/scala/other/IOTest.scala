// package other
// import java.io._
// import java.nio.file._
//
// import scala.concurrent.Future
// import scala.util.{Failure, Success}
//
// import hoge.{DBIO, ID}
//
// object IOTest {
//  def main(args: Array[String]): Unit = {
//     val in = new File("/tmp/test.jpeg")
//     val out = new File("/tmp/out.jpeg")
//
//     val len = in.length
//     println(len)
//
//     val os: OutputStream = new FileOutputStream(out)
//
//     val image = Files.readAllBytes(in.toPath)
//     os.write(image)
//     os.flush
//     os.close
// }
//}
//
//
//object hoge{
//  type ID = Long
//  case class DBIO[A](value: A)
//}
//// input形式
//case class JobBoardPageScore(
//  corporateId: ID,
//  corporateIdAlias: String,
//  score: Long,
//  jobs: Seq[JobDetailScore]
//) {
//  def getScoreWithJobId(id: ID): JobDetailScore = {
//    jobs.filter(_.jobId == id).head
//  }
//}
//
//case class JobDetailScore(
//  jobId: ID,
//  jobIdAlias: String,
//  score: Long,
//  similarities: Map[ID,Long]// jobId,SImilarity
//)
//
//object Conf {
//  val ScoreBoarder: Long = 10
//  val SimilarityBoarder: Long = 0.9L
//}
//
//class IndexService{
//
//  /**
//    * index対象の求人を取得する。
//    * @param jobs
//    * @return
//    */
//  def selectIndexJobs(jobs: Seq[JobDetailScore]): Seq[JobDetailScore] = {
//    val targetJobs = jobs.filter(_.score > Conf.ScoreBoarder)
//
//    targetJobs.filter{ job =>
//      // 似ている求人のスコアを取得
//      val jobIds =job.similarities.filter{case (id,sim) => sim > Conf.SimilarityBoarder}.keys
//      val scores =jobs.filter(job => jobIds.exists(_ == job.jobId)).map(_.score)
//
//      // 自分よりScoreの高い求人が存在しなければ、index対象
//      // 比較対象がない時もtrueが変える。
//      scores.forall(score => job.score > score)
//    }
//  }
//}
//
//object Test{
//  val path = new File("/tmp").toPath
//
//  List(path,path,path).foldLeft(Future.successful()){(acc,p) =>
//    acc.flatMap{ _ =>
//      Service.update(p)
//    }
//  } onComplete{
//    case Success(_) => println("成功をstatemachineに伝える。")
//    case Failure(_) => println("失敗をstatemachineniに伝える")
//  }
//}
//
//object Service {
//  val indexService = new IndexService
//
//  def update(path: Path):Future[Unit] = {
//    // 解析結果を取得
//    val data = parseJson(getJsonString(path))
//
//    // index対象をデータに入れる。
//    for{
//      _ <- query(data)
//      _ <- query2(data)
//    } yield ()
//
//    // db.run()
//  }
//
//  private def query(data: JobBoardPageScore):Future[Int] = {
//    val isIndex = data.score match {
//      case score if score >= Conf.ScoreBoarder => true
//      case score => false
//    }
//    JobBoardPageScoreRepository.insertCorporatePage(data,isIndex)
//  }
//
//  private def query2(data: JobBoardPageScore):Future[Int] = {
//    val indexJobs = indexService.selectIndexJobs(data.jobs)
//     JobBoardPageScoreRepository.insert(data.corporateId,indexJobs,true)
//   }
//
//   private def getJsonString(path: Path): String = {
//     val data = Files.readAllBytes(path)
//     new String(data)
//   }
//
//   // json文字列を変換する。
//   private def parseJson(json: String) : JobBoardPageScore = ???
//
// }
//
// object JobBoardPageScoreRepository {
//   // スコアをリセットする｡
//   def delete(): DBIO[Int] = ???

//   def insertCorporatePage(jobBoardPageScore: JobBoardPageScore,isindex: Boolean):Future[Int] = ???
//
//   // スコアをデータに反映させる｡
//   def insert(corporateId: Long, jobs: Seq[JobDetailScore],isIndex: Boolean): Future[Int] = ???
// }
