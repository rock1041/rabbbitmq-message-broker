package scala.publisher

import com.rabbitmq.client.{ConnectionFactory, Connection, Channel}
import java.nio.charset.StandardCharsets

object SimplePublisher extends App {
  private val QUEUE_NAME = "SimpleQueue"

  val factory = new ConnectionFactory()
  factory.setHost("localhost")
  factory.setPort(5672)

  // The try-with-resources syntax in Java can be replicated in Scala
  var connection: Connection = null
  var channel: Channel = null

  try {
    connection = factory.newConnection()
    channel = connection.createChannel()

    channel.queueDeclare(QUEUE_NAME, false, false, false, null) // Declare queue
    val message = "Hello Mr Rakesh!"
    channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8))
    println(s" [x] Sent '$message'")

  } catch {
    case e: Exception => e.printStackTrace()
  } finally {
    // Ensure resources are closed
    if (channel != null) channel.close()
    if (connection != null) connection.close()
  }
}
