
import org.apache.spark
import org.apache.spark.SparkConf
import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import org.apache.spark.ml.linalg.SparseVector
import org.apache.spark.sql.SparkSession
import org.jblas.DoubleMatrix

// 需要的数据源是电影内容信息
case class Movie(mid: Int, name: String, descri: String, timelong: String, issue: String,
                 shoot: String, language: String, genres: String, actors: String, directors: String)

case class MongoConfig(uri:String, db:String)

// 定义一个基准推荐对象
case class Recommendation( mid: Int, score: Double )

// 定义电影内容信息提取出的特征向量的电影相似度列表
case class MovieRecs( mid: Int, recs: Seq[Recommendation] )

object ContentRecommender{

  // 定义表名和常量
  val MONGODB_MOVIE_COLLECTION = "Movie"

  val CONTENT_MOVIE_RECS = "ContentMovieRecs"

  def main(args: Array[String]): Unit ={

    val config =  Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://localhost:27017/recommender",
      "mongo.db"->"recommender"
    )

    val sparkConf = new SparkConf().setMaster(config("spark.cores")).setAppName("offlineRecommender")

    // 创建一个SparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()

    import spark.implicits._

    implicit val  mongoConfig=  MongoConfig(config("mongo.uri"),config("mongo.db"))

    // 加载数据，并作预处理
   val movieTagsDF = spark.read
     .option("uri",mongoConfig.uri)
     .option("collection",MONGODB_MOVIE_COLLECTION)
     .format("com.mongodb.spark.sql")
     .load()
     .as[Movie]
     .map(
       // 提取mid,name,genres 三项内容作为原始内容特征，分词器默认按照空格做分词
       x => (x.mid, x.name, x.genres.map(c => if (c == '|') ' ' else c))
     )
     .toDF("mid", "name", "genres")
     .cache()

    // 核心部分，用TF-IDF从内容信息中提取电音特征向量

    val tokenizer = new Tokenizer().setInputCol("genres").setOutputCol("words")



  }
}