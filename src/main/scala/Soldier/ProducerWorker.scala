package Akka_With_Kafka.src.main.scala.Soldier

import Akka_With_Kafka.src.main.scala.config._

import java.util.Properties

import akka.actor.{Actor, ActorLogging}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}


class ProducerWorker extends Actor with ActorLogging {

  def defineProps(): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", KafkaConfig.BrokenNode)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props
  }


  def writeMsg(topic: String, key:  String, value: String)(implicit producer: KafkaProducer[String, String]): Unit = {
    val producerMsg = new ProducerRecord[String, String](topic, key, value)
    producer.send(producerMsg)
  }


  def closeProducerActor()(implicit producer: KafkaProducer[String, String]): Unit = {
    producer.close()
  }


  def receive: Receive = {

    case GenerateData(content, data) =>
      log.info(s"Task content: $content")
      log.info(s"Task Data: $data")

      implicit val producer = new KafkaProducer[String, String](this.defineProps())
      val workerTopic = "test"
      log.info(s"This is producer object $producer")
      log.info("Successfully call producer worker to do their job !")
      val p = context.self.path
      //      this.writeMsg(producer, s"this is key_$p", s"$data")
      this.writeMsg(workerTopic, s"this is key_test", s"$data")
      this.closeProducerActor()
      context.parent ! ProduceDone

  }

}

