package com.recommender

import com.mongodb.casbah.Imports.{MongoClient, MongoClientURI}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis
import com.mongodb.casbah.commons.MongoDBObject

import scala.collection.JavaConversions.{asScalaBuffer, mutableMapAsJavaMap}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.math.Ordered.orderingToOrdered

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2024/7/22 17:09
 */
// 连接助手对象
object ConnHelper extends Serializable{

  lazy val jedis = new Jedis("localhost")

  lazy val mongoClient = MongoClient(MongoClientURI("mongodb://influxdb:27017/recommender"))
}
case class MongoConfig(url: String,db: String)

// 标准推荐
case class Recommendation(mid:Int,score:Double)

//用户推荐
case class UserRecs(uid:Int,recs:Seq[Recommendation])

// 电影相似度
case class MovieRecs(mid:Int,recs:Seq[Recommendation])

// 电影相似度推荐
object StreamingRecommender {

  val MAX_USER_RATINGS_NUM = 20
  val MAX_SIM_MOVIES_NUM = 20
  val MONGODB_STREAM_RECS_COLLECTION = "StreamRecs"
  val MONGODB_RATING_COLLECTION = "Rating"
  val MONGODB_MOVIE_RECS_COLLECTION = "MovieRecs"

  // 入口方法
  def main(args: Array[String]): Unit = {

    val config = Map(

      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://influxdb:27017/recommender",
      "mongo.db" -> "recommender",
      "kafka.topic" -> "recommender"
    )

    // 创建一个sparkConf配置
    val sparkConf = new SparkConf()
      .setAppName("StreamingRecommender")
      .setMaster(config("spark.cores"))

    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val sc = spark.sparkContext
    val ssc = new StreamingContext(sc, Seconds(2))
    implicit val mongConfig = MongoConfig(config("mongo.uri"), config("mongo.db"))
    import spark.implicits._

    // 广播电影相似度矩阵
    val simMoviesMatrix = spark
      .read
      .option("uri", config("mongo.uri"))
      .option("collection", MONGODB_MOVIE_RECS_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[MovieRecs]
      .rdd
      .map { recs =>
        (recs.mid, recs.recs.map(x => (x.mid, x.score)).toMap)
      }.collectAsMap()
    val simMoviesMatrixBroadCast = sc.broadcast(simMoviesMatrix)

    // 创建到kafka的连接
    val kafkaPara = Map(
      "bootstrap.servers" -> "influxdb:9092",

      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "recommender",
      "auto.offset.reset" -> "latest"
    )

    val kafaStream = KafkaUtils
      .createDirectStream[String, String](ssc, LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe[String, String](Array(config("kafka.topic")), kafkaPara))

    // UID|MID|SCORE|TIMESTAMP
    // 产生评分流
    val ratingStream = kafaStream.map { case msg =>
      var attr = msg.value().split("\\|")
      (attr(0).toInt, attr(1).toInt, attr(2).toDouble, attr(3).toInt)
    }
    // 核心实时推荐算法
    ratingStream.foreachRDD { rdd =>
      rdd.map {
        case (uid, mid, score, timestamp) =>
          println(">>>>>>>>>>")

          // 获取当前最近的M次电影评分
          val userRecentlyRatings = getUserRecentlyRatings(MAX_USER_RATINGS_NUM, uid, ConnHelper.jedis)

          // 获取电影P最相似的k个电影
          val simMovies = getTopSimMovies(MAX_SIM_MOVIES_NUM, mid, uid, simMoviesMatrixBroadCast.value)

          // 计算待选电影的推荐优先级
          val streamRecs = computeMovieScores(simMoviesMatrixBroadCast.value, userRecentlyRatings, simMovies)

          // 将数据存到MongoDB
          saveRecsToMongoDB(uid, streamRecs)
      }.count()
    }
    // 启动 Streaming程序
    ssc.start()
    ssc.awaitTermination()
  }

  private def saveRecsToMongoDB(uid: Int, streamRecs: Array[(Int, Double)]) = {
    //到 StreamRecs 的连接

    val streaRecsCollection =
      ConnHelper.mongoClient(MongoConfig)(MONGODB_STREAM_RECS_COLLECTION)
      streaRecsCollection.findAndRemove(MongoDBObject("uid" -> uid))
      streaRecsCollection.insert(MongoDBObject("uid" -> uid, "recs" ->
      streamRecs.map( x => MongoDBObject("mid"->x._1,"score"->x._2)) ))


  }

  def computeMovieScores(simMovies:scala.collection.Map[Int,scala.collection.immutable.Map[Int,Double]],userRecentlyRatings:Array[(Int,Double)],topSimMovies: Array[Int]): Array[(Int,Double)] = {

    // 用于保存每一个待选择电影和最近评分得每一个电影的权重得分
    val score = ArrayBuffer[(Int, Double)]()

    // 用于保存每一个电影的增强因子数
    val increMap = scala.collection.mutable.HashMap[Int, Int]()

    //用于保存每一个电影的减弱因子数
    val decreMap = scala.collection.mutable.HashMap[Int, Int]()

    for (topSimMovie <- topSimMovies; userRecentlyRating <- userRecentlyRatings) {

      val simScore = getMoviesSimScore(simMovies, userRecentlyRating._1, topSimMovie)
      if (simScore > 0.6) {
        score += ((topSimMovie, simScore * userRecentlyRating._2))
      }
      if (userRecentlyRating._2 > 3) {
        increMap(topSimMovie) = increMap(topSimMovie) = increMap.getOrDefault(topSimMovie, 0) + 1
      }
      else {
        decreMap(topSimMovie) = decreMap.getOrDefault(topSimMovie, 0) + 1
      }
    }
    score.groupBy(_._1).map{
      case (mid,sims) => (mid,sims.map(_._2).sum / sims.length +logs(increMap.getOrDefault(mid,1))-logs(decreMap.getOrDefault(mid,1)))
    }.toArray.sortWith(_._2 >_._2)
  }

  def getMoviesSimScore(simMovies:scala.collection.Map[Int,scala.collection.immutable.Map[Int,Double]],

                         userRatingMovie:Int, topSimMovie:Int):Double ={

    val value = simMovies.get(topSimMovie) match {
      case Some(sim) => sim.get(userRatingMovie) match {
        case Some(score) => score
        case None => 0.0
      }
      case None => 0.0
    }
  }


  def logs(m:Int):Double ={
    math.log(m) / math.log(10)
  }

    def getTopSimMovies(num:Int, mid:Int, uid:Int, simMovies:scala.collection.Map[Int,scala.collection.immutable.Map[Int,Double]])(implicit mongoConfig: MongoConfig): Array[Int] = {

    // 从广播变量的电音相似度矩阵中获取当前电影所有的相似电影
    val allSimMovies = simMovies.get(mid).get.toArray

    // 获取用户已经观看过的电音
    val ratingExists = ConnHelper.mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).find(MongoDBObject("uid" -> uid)).toArray.map {
      item =>
        item.get("mid").toString.toInt
    }
  // 过滤掉已经评分的得电影，并排序输出
    allSimMovies.filter(x => !ratingExists.contains(x._1)).sortWith(_._2 >
      _._2).take(num).map(x => x._1)

  }

  def getUserRecentlyRatings(num: Int, uid: Int, jedis: Jedis): Array[(Int, Double)] = {

    // 从用户的队列取出num个评分

    jedis.lrange("uid:" + uid.toString, 0, num)
      .map { item =>
        val attr = item.split("\\:")
        (attr(0).trim.toInt, attr(1).trim.toDouble)
      }.toArray

  }
  

}


