package com.recommender

import com.mongodb.casbah.Imports.{MongoClient, MongoClientURI}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

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
case class MoviesRecs(mid:Int,recs:Seq[Recommendation])

// 电影相似度推荐
object StreamingRecommender{

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
    val sparkConf =new SparkConf()
      .setAppName("StreamingRecommender")
      .setMaster(config("spark.cores"))

   val spark = SparkSession.builder().config(sparkConf).getOrCreate()
   val sc = spark.sparkContext
   val ssc = new StreamingContext(sc, Seconds(2))
   implicit val mongConfig = MongoConfig(config("mongo.uri"),config("mongo.db"))
   import spark.implicits._

 }

}


