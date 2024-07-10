package com.recommender.statistics

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2024/7/10 9:39
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
case class MongoConfig(uri:String, db:String)
//定义一个基准推荐对象
case class Recommendation(mid:Int, score:Double)
// 定义电影类别top10推荐对象
case class GenersRecommendation(genres:String,recs:Seq[Recommendation])

object StatisticRecommender {
  // 所有电影类别
  val genres = List("Action", "Adventure", "Animation","Comedy", "Document","Drama", "Family", "Fantasy", "Foreign", "History", "Horror","Music", "Mystery", "Romance","Science","TV","Thriller","War","Western")
  val MONGODB_MOVIE_COLLECTION="Movie"
  val MONGODB_RATING_COLLECTION="Rating"
  val MONGODB_TAG_COLLECTION="Tag"
  val ES_MOVIE_INDEX = "Movie"

  //统计的表的名称

  val RATE_MORE_MOVIES = "RateMoreMovies"
  val RATE_MORE_RECENTLY_MOVIES = "RateMoreRecentlyMovies"
  val AVERAGE_MOVIES = "AverageMovies"
  val GENRES_TOP_MOVIES = "GenresTopMovies"


  def main(args: Array[String]): Unit = {
    val config = Map("spark.cores"-> "local[*]",
      "mongo.uri"->"mongodb://192.168.10.105:27017/recommender",
      "mongo.db"->"recommender",
      "es.httpHosts"->"localhost:9200",
      "es.transportHosts"->"localhost:9300",
      "es:index"->"recommender",
      "es.cluster.name"->"elasticsearch")

    //创建 SparkConf 配置

    val sparkConf = new SparkConf().setAppName("StatisticsRecommender").setMaster(config("spark.cores"))
    //创建 SparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    implicit val mongoConfig = MongoConfig(config("mongo.uri"),config("mongo.db"))
    //加入隐式转换
    import spark.implicits._

    // 加载数据
   val ratingDF= spark.read
                .option("uri",mongoConfig.uri)
                .option("collection",MONGODB_RATING_COLLECTION)
                .format("com.mongodb.spark.sql")
                .load()
                .as[Rating]
                .toDF()

    val movieDF = spark.read
      .option("uri",mongoConfig.uri)
      .option("collection",MONGODB_MOVIE_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[Movie]
      .toDF()

      // 创建一张名为ratings的表
    ratingDF.createOrReplaceTempView("ratings")

    // TODO 不同的统计推荐结果
    // 1.历史热门统计 历史评分最多,min,count,并把结果写入到对应的Mongo中
    val rateMoreMoviesDF = spark.sql("select mid,count(mind) as count from ratings group by mid")
    storeDFInMongoDB(rateMoreMoviesDF,RATE_MORE_MOVIES)
    // 2.近期热门统计，按照“yyyMM筛选最近得评分数据，统计评分个数
    // 创建日期格式化工具
    val simpleDateFormat = new SimpleDateFormat("yyyyMM")
    //注册UDF，把时间戳转为年月格式
    spark.udf.register("changeDate",(x:Int)=>simpleDateFormat.format(new Date(x*1000L)).toInt)

    // 对原始数据做预处理，去掉uid
    val ratingOfYearMonth = spark.sql("select mid,score,changeDate(timestamp) as  yearmonth from ratings")
      .createOrReplaceTempView("ratingOfMonth")
    // 从ratingofMonth中查找电影在各个月份的评分,mid,count,yearMonth
    val ratMoreRecentlyMoviesDF = spark.sql("select mid,count(mid) as count yearmonth from ratingOfMonth group by yearmonth,mid order by yearmonth desc,count desc  ")
    // 存入mongoDB
    storeDFInMongoDB(rateMoreMoviesDF,RATE_MORE_RECENTLY_MOVIES)
    // 3.优质电影推荐,统计电影的平均评分
    val averageMoviesDF = spark.sql("select mid,avg(score) as avg from ratings group by mid")
    storeDFInMongoDB(averageMoviesDF,AVERAGE_MOVIES)
    // 4.各类别电影评分Top统计

    spark.stop()
  }

   def storeDFInMongoDB(df: DataFrame, collectionName: String)(implicit mongoConfig: MongoConfig) ={

     df.write.option("uri",mongoConfig.uri)
       .option("collection",collectionName)
       .mode("overwrite")
       .format("com.mongodb.spark.sql")
       .save()

   }
}
