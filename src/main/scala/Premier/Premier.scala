package Akka_With_Kafka.src.main.scala.Premier

import Akka_With_Kafka.src.main.scala.config._
import Akka_With_Kafka.src.main.scala.Paladin.{ProducerLeader, ConsumerLeader}

import akka.actor.{Actor, ActorLogging, Props}


class Premier extends Actor with ActorLogging {

  def receive: Receive = {

    case DataComing(content) =>
      log.info("Got it !")
      log.info("Hello, Producer department guys, we have task right now.")
      val senderPathRef = context.actorSelection(context.self.path)
      println("[DEBUG] self path: " + senderPathRef)
      val kafkaProducerLeaderRef = context.actorOf(Props[ProducerLeader], KafkaActorName.ProducerLeaderName)
      val kafkaProducerLeader = context.actorSelection(kafkaProducerLeaderRef.path)
      KafkaActorName.ProducerLeaderPath = kafkaProducerLeaderRef.path.toString
      kafkaProducerLeader ! CallKafkaProducer("Here are some data detail.", "This should be the target data, I try it now !")


    case WaitData(content, taskNumber) =>
      log.info("Got it !")
      log.info("Hello, Consumer department guys, please wait for data, thank you.")
      val kafkaConsumerLeaderRef = context.actorOf(Props[ConsumerLeader], KafkaActorName.ConsumerLeaderName)
      val kafkaConsumerLeader = context.actorSelection(kafkaConsumerLeaderRef.path)
      KafkaActorName.ConsumerLeaderPath = kafkaConsumerLeaderRef.path.toString
      kafkaConsumerLeader ! CallKafkaConsumer("Hello, Consumer department guys, please wait for data, thank you.", taskNumber)

  }

}

