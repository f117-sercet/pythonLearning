package com.recommneder

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2024/7/5 16:38
 */
case class Movie(
                  mid: Int,
                  name: String,
                  descri:String,
                  timelong:String,
                  issue:String,
                  shoot:String,
                  language:String,
                  genes:String,
                  actors:String,
                  directors:String)
case class Rating(uid:Int,
                  mid:Int,
                  score:Double,
                  timeStamp:Long)
case class Tag(uid:Int,
               mid:Int,
               tag:String,
               timeStamp:Long)

case class MongoDBConf(uri:String,db:String)
case class ESConf(
                   httpHosts:String,
                   transportHosts:String,
                   index:String,
                   clustername:String)
object DataLoader {


   val MOVIE_DATA_PATH ="F:\\git\\Repository\\gitHub\\pythonLearning\\recommendSystem\\MovieRecommend\\recommender\\DataLoader\\src\\main\\resources\\movies.csv"
   val RATING_DATA_PATH ="F:\\git\\Repository\\gitHub\\pythonLearning\\recommendSystem\\MovieRecommend\\recommender\\DataLoader\\src\\main\\resources\\rating.csv"
  val TAG_DATA_PATH ="F:\\git\\Repository\\gitHub\\pythonLearning\\recommendSystem\\MovieRecommend\\recommender\\DataLoader\\src\\main\\resources\\tags.csv"
  def main(args: Array[String]): Unit = {



    val config = Map("spark.cores"-> "local[*]",
      "mongo.uri"->"mongodb://influxdb:27017/recommender",
    "mongo.db"->"recommender",
    "es.httpHosts"->"localhost:9200",
    "es.transportHosts"->"localhost:9300",
    "es:index"->"recommender",
    "es.cluster.name"->"elasticsearch")
    // 创建sparkConfig对象
    val sparkConf = new SparkConf().setMaster(config("saprk.cores")).setAppName("DataLoader")

    // 创建sparkSession
    val spark = SparkSession.builder().config(sparkConf)getOrCreate()

    // 隐式转换
    import spark.implicits._



    // 加载数据
    val movieRDD = spark.sparkContext.textFile(MOVIE_DATA_PATH)
    val ratingRDD = null
    val tagRDD = null

    // 数据预处理

    // 将数据保存到MongoDB
    storeDataInMongoDB();
    
    // 保存到ES
    storeDataInES();

    spark.stop()
  }

  private def storeDataInMongoDB() = ???

  private def storeDataInES() = ???



}
