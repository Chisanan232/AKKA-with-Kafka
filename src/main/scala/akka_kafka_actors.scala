package Akka_With_Kafka.src.main.scala

import Akka_With_Kafka.src.main.scala.config._
import Akka_With_Kafka.src.main.scala.King.DataKing

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.util.control.Breaks._
import scala.concurrent.Await

import akka.actor.{ActorSystem, Props}
import akka.event.Logging
import akka.util.Timeout
import akka.pattern.ask



object akka_kafka_actors extends App {

  val system = ActorSystem(KafkaActorName.AkkaSystemName)
  val kafkaActor = system.actorOf(Props[DataKing], KafkaActorName.DataKingActor)

  val log = Logging(system.eventStream, "akka-with-kafka.log")

  // Start target task!
  kafkaActor ! DataExist

  // Wait for the moment about finish program
  implicit val timeout = Timeout(30.seconds)
  val finishPattern = "/w{0,32}Finish".r
  breakable(
    while (true) {
      val processingResp = kafkaActor ? ProcessingAsk
      val result = Await.result(processingResp, timeout.duration)
      log.info(s"The response of Master actor: $result")
      if (result.toString.equals("Program Finish.")) {
        log.info("Does it shutdown the program?")
        system.terminate()
        break()
        log.info("does it break out of the loop?")
      }
      log.info("Will sleep for 10 seconds to keep going ask Master actor the work processing ..,")
      Thread.sleep(100)
    }
  )

  log.info("Program Finish!")

}


object TestDateGeneration extends App {

  import org.joda.time.{DateTime, Period}


  def dateRange(from: DateTime, to: DateTime, step: Period): Iterator[DateTime] = {
    Iterator.iterate(from)(_.plus(step)).takeWhile(!_.isAfter(to))
  }

  val fromDate = new DateTime().withYear(2000).withMonthOfYear(2).withDayOfMonth(27)
  println(fromDate)
// Will error here.
  //  val testDate2 = new DateTime().withYear(2013).withMonthOfYear(2).withDayOfMonth(30)
//  println(testDate2)

  val toDate = new DateTime().withYear(2013).withMonthOfYear(6).withDayOfMonth(1)
  println(toDate)

  val allDateData = {dateRange(fromDate, toDate, new Period().withDays(1))}
//  println(allDateData.toList)
  var dateList = new ListBuffer[String]()
  allDateData.foreach(ele => {
    println(ele.getYear)
    println(ele.getMonthOfYear)
    println(ele.getDayOfMonth)
    val date_year = ele.getYear.toString
    val date_month = ele.getMonthOfYear.toString
    val date_day = ele.getDayOfMonth.toString
    dateList += List(date_year, date_month, date_day).mkString("/")
    println("-----------------------")
  })

  println(dateList)
  println(DateTime.now())

//  for (date <- allDateData) {
//    println(date.getYear)
//    println(date.getMonthOfYear)
//    println(date.getDayOfMonth)
//    println("-----------------------")
//  }
//  println(allDateData.toList.head.toString().split("T"))
//  println(allDateData.toList.head.toString().split("T").head)

}
