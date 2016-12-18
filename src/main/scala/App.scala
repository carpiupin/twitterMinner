import org.apache.spark._
import org.apache.spark.streaming._
import twitter4j.auth.OAuthAuthorization
import twitter4j.conf.ConfigurationBuilder
import org.apache.spark.streaming.twitter.TwitterUtils

object Main {
  def main(args: Array[String]) {
    val logFile = "/home/piury/PIURY/Autoridacta/spark/spark-2.0.2-bin-hadoop2.7/README.md"
    val conf = new SparkConf().setMaster("local").setAppName("Simple App")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    val ssc = new StreamingContext(sc, Seconds(5))

    System.setProperty("twitter4j.oauth.consumerKey", ConfigLoader.consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", ConfigLoader.consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", ConfigLoader.accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", ConfigLoader.accessTokenSecret)

    val configurationBuilder = new ConfigurationBuilder()
    val oAuth = Some(new OAuthAuthorization(configurationBuilder.build()))

    val rawTweets = TwitterUtils.createStream(ssc, oAuth)

    sc.stop()
  }
}