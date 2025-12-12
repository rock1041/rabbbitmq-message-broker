package subscriber

import com.rabbitmq.client.{CancelCallback, ConnectionFactory, DeliverCallback}
import org.slf4j.LoggerFactory

import java.nio.charset.StandardCharsets

object RealWorldSubscriber {

  private val QUEUE_NAME = "SimpleQueue"
  private val EXCHANGE_NAME = "logs"
  private val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {

    val factory = new ConnectionFactory()

    factory.setHost("localhost")
    factory.setPort(5672)

    val connection = factory.newConnection()
    val channel = connection.createChannel()

    //channel.queueDeclare(QUEUE_NAME, false, false, false, null)
    channel.exchangeDeclare(EXCHANGE_NAME, "fanout")
    val queueName = channel.queueDeclare().getQueue
    channel.queueBind(queueName, EXCHANGE_NAME, "")

    println(" [*] Waiting for messages. To exit press CTRL+C")

    val deliverCallback: DeliverCallback = (consumerTag: String, delivery: com.rabbitmq.client.Delivery) => {
      val message = new String(delivery.getBody, StandardCharsets.UTF_8)
      println(s" [x] Received '$message'")

    }

    val cancelCallback: CancelCallback = (consumerTag: String) => {}

    // Auto-acknowledgment is set to true for simplicity in this basic example
    //channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag => { } )
    channel.basicConsume(queueName, true, deliverCallback, cancelCallback)

  }
}