package com.recommender.offlineRecommender

import breeze.numerics.sqrt
import org.apache.spark.SparkConf
import org.apache.spark.mllib.recommendation.{ALS, MatrixFactorizationModel, Rating}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2024/7/18 17:31
 */
object ALSTrainer {
  def main(args: Array[String]): Unit = {
    val config = Map(
      "spark.cores" -> "local[*]", "mongo.uri" -> "mongodb://localhost:27017/recommender",
      "mongo.db" -> "recommender"
    )
    // 创建 SparkConf
    val sparkConf = new SparkConf().setAppName("ALSTrainer").setMaster(config("spark.cores"))

  // 创建SparkSession
  val spark = SparkSession.builder().config(sparkConf).getOrCreate()
    val mongoConfig = MongoConfig(config("mongo.uri"),config("mongo.db"))
    import spark.implicits._

    val ratingRDD = spark.read
      .option("uri", mongoConfig.uri)
      .option("collection", OfflineRecommender.MONGODB_RATING_COLLECTION)
      .format("com.mongodb.spark.sql")
      .load()
      .as[MovieRating]
      .rdd
      .map(rating => (Rating(rating.uid, rating.mid, rating.score))).cache()

    // 将一个RDD随机切分成两个RDD,用以划分训练集和测试集
    val splits = ratingRDD.randomSplit(Array(0.8, 0.2))
    val trainingRDD = splits(0)
    val testingRDD = splits(1)
    
    // 输出最优参数
    adjustALSParams(trainingRDD,testingRDD)
    
    // 关闭spark
    spark.close()
    
    
  }

  //输出最终的最优参数
  def adjustALSParams(trainData:RDD[Rating], testData:RDD[Rating]): Unit = {

    //	这里指定迭代次数为 5，rank 和 lambda 在几个值中选取调整
    val result = for(rank <- Array(100,200,250);lambda <-Array(1,0.1,0.01,0.001))
      yield{
        val model = ALS.train(trainData, rank, 5, lambda)
        var rmse = GetRMSE(model,testData)
        (rank,lambda,rmse)
      }
    // 按照rmse 排序
    println(result.sortBy(_._3).head)
  }

  def GetRMSE(model:MatrixFactorizationModel, data:RDD[Rating]):Double= {

    val userMovies = data.map(item => (item.user, item.product))
    val predictRating = model.predict(userMovies)
    val real = data.map(item => ((item.user,item.product),item.rating))
    val predict = predictRating.map(item => ((item.user,item.product),item.rating))

    // 计算 RMSE
    sqrt(
      real.join(predict).map{case ((uid,mid),(real,pre))
      // 真实值和预测值
       => var err =real -pre
        err*err
      }.mean()
    )

  }

}
