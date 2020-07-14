# AKKA-with-Kafka


### Description
This is a sample code about program with AKKA software architecture and integrate with Kafka mechanism. <br>
<br>

### Motivation
Study and note this for myself to learn a new skill and knowledge. Of cource, I also hope this could help the people who is new in this skill. <br>
<br>

### Skills
Language: Scala <br>
Version: 2.12 <br>
Framework: AKKA (version: 2.4.20) <br>
Message Queue Server: Kafka (version: 2.5.0) <br>

#### Environment
OS: MacOS (Current Version: 10.14.5)
<br>

### Pre-Process
Need to install Kafka in environment (Or current environment could connect to Kafka broker) before start this project. <br>

Here are some basic elements of Kafka: <br>
* Broker <br>
Basic unit which could provide Kafka service. A cluster system could be composed of multiple brokers.

* Producer <br>
The application which produces message and send it to Kafka broker.

* Consumer <br>
The application which consumes message from Kafka broker.

* Topic <br>
A category name about saving-data to store or publish.

* Partition <br>
A Kafka topic could be divided to multiple partitions. It's the smallest physically unit of saving data. <br>
For option 'replication', it implement at partition level. In other words, option 'replication' and partition is related directly. That means how many replication amount it has, how many partition has data be recorded in Kafka. <br>

<br>

Kafka is dependence to Zookeeper, so it's necessary to run zookeeper server before run Kafka service. <br>

    zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties

'/usr/local/etc/kafka/' is the directory path where save kafka configuration.

Run Kafka server after activate Zookeeper. <br>

    kafka-server-start /usr/local/etc/kafka/server.properties

It's necessary that add a Topic which is the destination the message will be send to. <br>

    kafka-topics --bootstrap-server <Kafka boker IP address with port, ex: localhost:9092> --create --topic <topic name> --partitions <partitions amount>

Verify the Topic has be created successfully. <br>
    
    kafka-topics --bootstrap-server <Kafka boker IP address with port> --list

Check the detail info of the Topic. <br>

    kafka-topics --bootstrap-server <Kafka boker IP address with port> --topic <topic name> --describe

Running-result of command line: <br>
![](https://github.com/Chisanan232/AKKA-with-Kafka/raw/master/docs/imgs/kafka-topics-describe.png)

Access to the topic as Consumer via command line. <br>

    kafka-console-consumer --bootstrap-server <Kafka boker IP address with port> --topic <topic name>

It could pointed partition with option "--partition <partition number>". It also could use option "--from-beginning" to get all message of target topic. <br>

Access to the topic as Producer via command line. <br>

    kafka-console-producer --bootstrap-server <Kafka boker IP address with port> --topic <topic name>

Send message to Kafka by Producer and get the meesage by Consumer. <br>

If it be needed to shutdown Kafka service, please close it by command line: <br>

    kafka-server-stop /usr/local/etc/kafka/server.properties

For Zookeeper, so as <br>

    zookeeper-server-stop /usr/local/etc/kafka/zookeeper.properties


Code Description
===
Some explanation about code which implement Kafka feature. <br>

Producer
---

Consumer
---

