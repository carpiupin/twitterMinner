import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.sql.SparkSession

object App {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster(ConfigLoader.masterEndpoint).setAppName("SimpleApp")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    val ssc = new StreamingContext(sc, Seconds(5))

    System.setProperty("twitter4j.oauth.consumerKey", ConfigLoader.consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", ConfigLoader.consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", ConfigLoader.accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", ConfigLoader.accessTokenSecret)

    val rawTweets = TwitterUtils.createStream(ssc, None)
    val statuses = rawTweets.map(status => status.getText())
    
    statuses.print()
    
    ssc.start()
    ssc.awaitTermination()
  }
}