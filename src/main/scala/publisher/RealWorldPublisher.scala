package publisher

import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}
import org.slf4j.LoggerFactory

import java.nio.charset.StandardCharsets

object RealWorldPublisher {
  private val QUEUE_NAME = "SimpleQueue"
  private val EXCHANGE_NAME = "logs"
  private val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {

    val factory = new ConnectionFactory()
    factory.setHost("localhost")
    factory.setPort(5672)

    // The try-with-resources syntax in Java can be replicated in Scala
    var connection: Connection = null
    var channel: Channel = null

    try {
      connection = factory.newConnection()
      channel = connection.createChannel()
      channel.exchangeDeclare(EXCHANGE_NAME, "fanout")
      //channel.queueDeclare(QUEUE_NAME, false, false, false, null) // Declare queue
      val message = args.mkString(" ")
      //channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8))
      channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"))
      println(s" [x] Sent '$message'")

    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      // Ensure resources are closed
      if (channel != null) channel.close()
      if (connection != null) connection.close()
    }
  }
}
