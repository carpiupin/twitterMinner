import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config


object ConfigLoader {
  private val conf: Config = ConfigFactory.load("application.conf")

  val consumerKey = conf.getString("CONSUMER_KEY")
  val consumerSecret = conf.getString("CONSUMER_SECRET")
  val accessToken = conf.getString("ACCESS_TOKEN_KEY")
  val accessTokenSecret = conf.getString("ACCESS_TOKEN_SECRET")
  
  val masterEndpoint = conf.getString("MASTER_ENDPOINT")
}