name := "Akka_With_Kafka"

version := "1.0"

scalaVersion := "2.12.11"

/*
 * Akka: https://doc.akka.io/docs/akka/2.3/intro/getting-started.html
 * Kafka: https://doc.akka.io/docs/alpakka-kafka/current/home.html#project-info
 */

libraryDependencies ++= {

  val AkkaVersion = "2.5.31"
  val KafkaVersion = "2.5.0"

  Seq(
    // Akka
    "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    // Kafka
    "org.apache.kafka" %% "kafka" % KafkaVersion,
    "org.apache.kafka" % "kafka-clients" % KafkaVersion,
    "org.apache.kafka" % "kafka-admin" % KafkaVersion,
//    // Other packages
    "log4j" % "log4j" % "1.2.16"
  )

}

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)
