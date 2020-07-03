package Akka_With_Kafka.src.main.scala.Paladin

import Akka_With_Kafka.src.main.scala.config._
import Akka_With_Kafka.src.main.scala.Soldier.ProducerWorker

import akka.actor.{Actor, ActorLogging, ActorRef, Props}


class ProducerLeader extends Actor with ActorLogging {

  val producerWorkerMemberNumber = 2

  def receive: Receive = {

    case CallKafkaProducer(content, data) =>
      log.info("Got it!")

      val producerWorkers = new Array[ActorRef](this.producerWorkerMemberNumber)
      for (workerID <- 0.until(this.producerWorkerMemberNumber)) {
        val workerName = KafkaActorName.ProducerWorkerName
        producerWorkers(workerID) = context.actorOf(Props[ProducerWorker], s"$workerName$workerID")
      }
      producerWorkers.foreach(producerWorkerRef => {
        val producerWorker = context.actorSelection(producerWorkerRef.path)
        producerWorker ! GenerateData("Hey, workers! You have job right now. Go Go Go!", s"$data")
      })

  }

}

