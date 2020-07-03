package Akka_With_Kafka.src.main.scala.Soldier

import Akka_With_Kafka.src.main.scala.config._

import java.util
import java.util.Properties

import scala.collection.JavaConverters._
import scala.util.control.Breaks._
import akka.actor.{Actor, ActorLogging}
import org.apache.kafka.clients.consumer.KafkaConsumer


class ConsumerWorker extends Actor with ActorLogging {

  val topic = "test"

  def defineProps(consumerGroupID: String): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", KafkaConfig.BrokenNode)
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("group.id", consumerGroupID)
    props
  }


  def workerTaskResponsibility(topic: String, partition: String)(implicit consumer: KafkaConsumer[String, String]): Unit = {
    // For creating multiple Kafka Consumer.
    // https://stackoverflow.com/questions/53918333/scala-how-to-subscribe-to-multiple-kafka-topics/53918419
    consumer.subscribe(util.Arrays.asList(topic, partition))
  }


  def getMsg(taskNumber: Int)(implicit consumer: KafkaConsumer[String, String]): Unit = {
    var currentTaskNumber = 0
    try {
      breakable(
        while (true) {
          val records = consumer.poll(500).asScala
          for (record <- records) {
            println(record)

            if (record.value() != None) {
              context.actorSelection(KafkaActorName.KingPath) ! FinishTask("We done the task !")
              currentTaskNumber + 1
              if (currentTaskNumber == taskNumber) {
                break()
              }
            }

          }
        }
      )
    } catch {
      case e: Exception => println("Exception occur: " + e.printStackTrace())
    } finally {
      println("[FINALLY] Run the  code at finally of logical-code area.")
    }
  }


  def closeConsumerActor()(implicit consumer: KafkaConsumer[String, String]): Unit = {
    consumer.close()
  }


  def receive: Receive = {

    case ConsumeData(content, consumerID, taskNumber) =>
      log.info(s"Task content: $content")
      log.info(s"consumer ID: $consumerID")

      val consumerGroupID = "test"
      implicit val consumer = new KafkaConsumer[String, String](this.defineProps(consumerGroupID))
      log.info(s"This is consumer object $consumer")
      log.info("Successfully call consumer worker to do their job.")
      this.workerTaskResponsibility("test", s"test_partition_$consumerID")
      this.getMsg(taskNumber)
      this.closeConsumerActor()

  }

}

