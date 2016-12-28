import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.sql.SparkSession
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import java.util.Date

object App {
  def main(args: Array[String]) {
    val Database = "twitterMinner"
    val Collection = "tweets"
    val MongoHost = "127.0.0.1"
    val MongoPort = 27017
    val streamingFreq = Seconds(6)
    //    val mongoClient = MongoClient(MongoHost, MongoPort)
    //    val collection = mongoClient(Database)(Collection)

    val conf = new SparkConf().setAppName("SimpleApp")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    val ssc = new StreamingContext(sc, streamingFreq)

    System.setProperty("twitter4j.oauth.consumerKey", ConfigLoader.consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", ConfigLoader.consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", ConfigLoader.accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", ConfigLoader.accessTokenSecret)

    val rawTweets = TwitterUtils.createStream(ssc, None)
    //    val statuses = rawTweets.map(status => ("@" + status.getUser().getScreenName() + ":" + status.getText()))

    println("\n=========== Processing tweets every %s ===========".format(Seconds(6)))

    rawTweets.foreachRDD { rdd =>
      val count = rdd.count()
      if (count > 0) {
        println("\nWriting tweets to mongo... (%s total):".format(count))
        rdd.foreachPartition { partition =>
          val mongoClient = MongoClient(MongoHost, MongoPort)
          val collection = mongoClient(Database)(Collection)
          partition.foreach(tweet => collection.insert {
            MongoDBObject("id" -> new Date(),
              "accountName" -> tweet.getUser().getScreenName(),
              "tweet" -> tweet.getText())
          })
        }
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }
}