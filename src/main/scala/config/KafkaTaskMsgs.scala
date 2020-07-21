package Akka_With_Kafka.src.main.scala.config

class KafkaTaskMsgs {

}

trait TaskMag {
  val content: String
}

case class DataExist(content: String) extends TaskMag
case class ProcessingAsk(content: String) extends TaskMag
case class DataComing(content: String, taskNumber: Int) extends TaskMag
case class DataRequire(content: String) extends TaskMag
case class WaitData(content: String, taskNumber: Int) extends TaskMag
case class CallKafkaProducer(content: String, data: Any, taskNumber: Int) extends TaskMag
case class CallKafkaConsumer(content: String, taskNumber: Int) extends TaskMag
case class GenerateData(content: String, data: Any) extends TaskMag
case class ConsumeData(content: String, consumerID: Int, taskNumber: Int) extends TaskMag
case class ProduceDone(content: String) extends TaskMag
case class ConsumeDone(content: String) extends TaskMag
case class FinishTask(content: String) extends TaskMag
