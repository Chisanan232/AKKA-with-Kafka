package Akka_With_Kafka.src.main.scala.King

import Akka_With_Kafka.src.main.scala.config._
import Akka_With_Kafka.src.main.scala.Premier.Premier

import akka.actor.{Actor, ActorLogging, Props}


class DataKing extends Actor with ActorLogging {

  import context.dispatcher

  val allTaskNUmber = 2
  var currentDoneTaskNumber = 0
  var shutdownSystemSignal = false

  def receive: Receive = {

    case DataExist =>
      log.info("I receive the task !")
      log.info("Hello, Premier. I need your help !")
      KafkaActorName.KingPath = self.path.toString
      val dataPremierRef = context.actorOf(Props[Premier], KafkaActorName.DataPremierName)
      val dataPremier = context.actorSelection(dataPremierRef.path)
      KafkaActorName.PremierPath = dataPremierRef.path.toString
      dataPremier ! DataComing("We have something to do, my man.", this.allTaskNUmber)
      dataPremier ! WaitData("Please wait for data.", this.allTaskNUmber)


    case FinishTask(content) =>
      log.info("[King] Good job, man !")
      println(s"[DEBUG] currentDoneTaskNumber: $currentDoneTaskNumber")
      currentDoneTaskNumber += 1
      if (currentDoneTaskNumber == this.allTaskNUmber) {
        log.info("We finish the project !")
        context.system.terminate()
        log.info("Terminate the AKKA actor System ....")
        this.shutdownSystemSignal = true
      }

  }

}

