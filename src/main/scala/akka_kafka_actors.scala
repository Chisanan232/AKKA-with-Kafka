package Akka_With_Kafka.src.main.scala

import Akka_With_Kafka.src.main.scala.config._
import Akka_With_Kafka.src.main.scala.King.DataKing
import Akka_With_Kafka.src.main.scala.Paladin.{ProducerLeader, ConsumerLeader}

import scala.concurrent.duration._

import akka.actor.{ActorSystem, Props}
import akka.event.Logging
import akka.util.Timeout



object akka_kafka_actors extends App {

  val system = ActorSystem(KafkaActorName.AkkaSystemName)
  val kafkaActor = system.actorOf(Props[DataKing], KafkaActorName.DataKingActor)

  val log = Logging(system.eventStream, "akka-with-kafka.log")

  // Start target task!
  kafkaActor ! DataExist

  // Wait for the moment about finish program
  implicit val timeout = Timeout(30.seconds)
  val finishPattern = "/w{0,32}Finish".r

  log.info("Program Finish!")

}


object KafkaProducerMain extends App {

  val taskNumber = 2

  val system = ActorSystem("KafkaProducer")
  val kafkaProducerLeader = system.actorOf(Props[ProducerLeader], KafkaActorName.ProducerLeaderName)
  KafkaActorName.ProducerLeaderPath = kafkaProducerLeader.path.toString
  kafkaProducerLeader ! CallKafkaProducer("Here are some data detail.", "This should be the target data, I try it now !", taskNumber)

}


object KafkaConsumerMain extends App {

  val taskNumber = 2

  val system = ActorSystem("KafkaProducer")
  val kafkaConsumerLeader = system.actorOf(Props[ConsumerLeader], KafkaActorName.ProducerLeaderName)
  KafkaActorName.ConsumerLeaderPath = kafkaConsumerLeader.path.toString
  kafkaConsumerLeader  ! CallKafkaConsumer("Hello, Consumer department guys, please wait for data, thank you.", taskNumber)

}
