package Akka_With_Kafka.src.main.scala.Paladin

import Akka_With_Kafka.src.main.scala.config._
import Akka_With_Kafka.src.main.scala.Soldier.ProducerWorker

import akka.actor.{Actor, ActorLogging, ActorRef, Props}


class ProducerLeader extends Actor with ActorLogging {

  val producerWorkerMemberNumber = 2
  var allTasks: Int = _
  var currentTasks = 0

  def receive: Receive = {

    case CallKafkaProducer(content, data, taskNumber) =>
      log.info("Got it!")
      allTasks = taskNumber

      val producerWorkers = new Array[ActorRef](this.producerWorkerMemberNumber)
      for (workerID <- 0.until(this.producerWorkerMemberNumber)) {
        val workerName = KafkaActorName.ProducerWorkerName
        producerWorkers(workerID) = context.actorOf(Props[ProducerWorker], s"$workerName$workerID")
      }
      producerWorkers.foreach(producerWorkerRef => {
        val producerWorker = context.actorSelection(producerWorkerRef.path)
        producerWorker ! GenerateData("Hey, workers! You have job right now. Go Go Go!", s"$data")
      })


    case ProduceDone =>
      currentTasks += 1
      log.info("Get the done signal.")
      if (currentTasks.equals(allTasks)) {
        log.info("Finish this task!")
        context.system.terminate()
      }

  }

}

