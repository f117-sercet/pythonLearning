package com.recommneder

import com.mongodb.casbah.MongoClient
import com.recommneder.DataLoader.storeDataInMongoDB
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

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

  val MONGODB_MOVIE_COLLECTION="Movie"
  val MONGODB_RATING_COLLECTION="Rating"
  val MONGODB_TAG_COLLECTION="Tag"
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
    val movieDF = movieRDD.map(item =>{
      val attr = item.split("\\^")
      Movie(attr(0).toInt, attr(1).trim, attr(2).trim, attr(3).trim, attr(4).trim, attr(5).trim, attr(6).trim, attr(7).trim, attr(8).trim, attr(9).trim)
    }
    ).toDF()

    //rating转换为 DF
    val ratingRDD = spark.sparkContext.textFile(RATING_DATA_PATH)
    val ratingDF = ratingRDD.map(item => {
      val attr = item.split(",")
      Rating(attr(0).toInt, attr(1).toInt, attr(2).toDouble, attr(3).toInt)
    }).toDF()

    val tagRDD = spark.sparkContext.textFile(TAG_DATA_PATH)
    val tagDF  = tagRDD.map(item=>{
      val attr = item.split(",")
      Tag(attr(0).toInt, attr(1).toInt, attr(2).trim, attr(3).toInt)
    }).toDF()

  implicit val mongoConfig= MongoDBConf(config("mongo.uri"),config("mongo.db"))

    // 将数据保存到MongoDB
    storeDataInMongoDB(movieDF,ratingDF,tagDF);

    // 数据预处理
    // 保存到ES
    storeDataInES();

    spark.stop()
  }

  private def storeDataInMongoDB(movieDF:DataFrame,ratingDF:DataFrame,tagDF:DataFrame)(implicit mongoConfig:MongoDBConf) = {

    //新建MongoDB连接
    val mongoClient = MongoClient(mongoConfig.uri)

  }

  private def storeDataInES() = ???



}