package Akka_With_Kafka.src.main.scala.Paladin

import Akka_With_Kafka.src.main.scala.config._
import Akka_With_Kafka.src.main.scala.Soldier.ConsumerWorker

import akka.actor.{Actor, ActorLogging, ActorRef, Props}


class ConsumerLeader extends Actor with ActorLogging{

  val consumerWorkerMemberNumber= 2
  var allTasks: Int = _
  var currentTasks = 0

  def receive: Receive = {

    case CallKafkaConsumer(content, taskNumber) =>
      log.info("Got it!")
      allTasks = taskNumber

      val consumerWorkers = new Array[ActorRef](this.consumerWorkerMemberNumber)
      for (workerID <- 0.until(this.consumerWorkerMemberNumber)) {
        val workerName = KafkaActorName.ConsumerWorkerName
        consumerWorkers(workerID) = context.actorOf(Props[ConsumerWorker], s"$workerName$workerID")
      }

      val eachConsumerTaskNumber = taskNumber / this.consumerWorkerMemberNumber

      consumerWorkers.foreach(consumerWorkerRef => {
        val consumerWorker = context.actorSelection(consumerWorkerRef.path)
        consumerWorker ! ConsumeData("Hey, consumers guys. We got the data to do something !", consumerWorkerRef.path.name.takeRight(1).toInt, eachConsumerTaskNumber)
      })


    case ConsumeDone =>
      currentTasks += 1
      log.info("Get the done signal.")
      if (currentTasks.equals(allTasks)) {
        log.info("Finish this task!")
        context.system.terminate()
      }

  }

}

