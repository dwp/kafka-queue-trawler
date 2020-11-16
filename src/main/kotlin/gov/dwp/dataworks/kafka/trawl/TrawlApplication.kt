package gov.dwp.dataworks.kafka.trawl

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import sun.misc.Signal
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.ExperimentalTime
import kotlin.time.seconds
import kotlin.time.toJavaDuration

@SpringBootApplication
class TrawlApplication(private val consumerProvider: () -> KafkaConsumer<ByteArray, ByteArray>,
                       private val initialOffset: Long) : CommandLineRunner {

    @ExperimentalTime
    override fun run(vararg args: String?) {
        consumerProvider().use { consumer ->
            consumer init args
            while (!closed.get()) {
                consumer.poll(10.seconds.toJavaDuration()).forEach(::printMetadata)
            }
        }
    }

    private infix fun <K, V> KafkaConsumer<K, V>.init(args: Array<out String?>) {
        this handleSignal "INT"
        this handleSignal "TERM"
        this subscribe args
    }

    private fun printMetadata(record: ConsumerRecord<ByteArray, ByteArray>) {
        println(record.metadata())
    }

    private fun <V> ConsumerRecord<ByteArray, V>.metadata(): String =
        "${String(key())}, ${timestamp()}, ${partition()}, ${offset()}"

    private infix fun <K, V> KafkaConsumer<K, V>.handleSignal(signalName: String) {
        logger.info("Setting up $signalName handler.")
        Signal.handle(Signal(signalName)) {
            logger.info("'$it' signal received, cancelling job.")
            closed.set(true)
            wakeup()
        }
    }

    private infix fun <K, V> KafkaConsumer<K, V>.subscribe(topics: Array<out String?>) {
        logger.info("Subscribing to '${topics.asList()}'.")

        subscribe(topics.asList(), object: ConsumerRebalanceListener {
            override fun onPartitionsRevoked(partitions: MutableCollection<TopicPartition>?) {
                logger.info("Partitions revoked: '$partitions'.")
            }

            override fun onPartitionsAssigned(partitions: MutableCollection<TopicPartition>?) {
                logger.info("Partitions assigned: '$partitions'.")
                when {
                    initialOffset > 0 -> {
                        partitions?.forEach { seek(it, initialOffset) }
                    }
                    else -> {
                        seekToBeginning(partitions)
                    }
                }
            }
        })
    }

    companion object {
        private val logger = LoggerFactory.getLogger(TrawlApplication::class.java)
        private val closed: AtomicBoolean = AtomicBoolean(false)
    }
}

fun main(args: Array<String>) {
    runApplication<TrawlApplication>(*args)
}
