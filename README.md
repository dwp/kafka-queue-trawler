# kafka-queue-trawler

## Simple kafka queue trawler that lists records on the queue.

After cloning this repo, please run:  
`make bootstrap`

## Usage in DataWorks

This is a utility script which can be run if needed in order to discover if a specific record is on a Kafka queue or not. When DataWorks were doing data evaluation work on the Kafka stream, this was very useful to see if we were picking up specific records or not.

It is a Kotlin application which can be run locally.

### Configuration

There are two main configuration classes, so set an app properties file locally to run which the following properties set for Kafka:

  data class KafkaProperties(var bootstrapServers: String = "",
     var consumerGroup: String = "topic-trawler",
     var useSsl: Boolean = false,
     var trustStore: String = "",
     var trustStorePassword: String = "",
     var keyStore: String = "",
     var keyStorePassword: String = "",
     var keyPassword: String = "",
     var initialOffset: Long = 0)
