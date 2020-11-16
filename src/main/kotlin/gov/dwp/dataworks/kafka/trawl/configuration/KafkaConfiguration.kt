package gov.dwp.dataworks.kafka.trawl.configuration

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

@Configuration
@ExperimentalTime
class KafkaConfiguration(private val bootstrapServers: String,
                         private val consumerGroup: String,
                         private val useSsl: Boolean,
                         private val trustStore: String,
                         private val trustStorePassword: String,
                         private val keyStore: String,
                         private val keyStorePassword: String,
                         private val keyPassword: String) {

    @Bean
    fun consumer(): () -> KafkaConsumer<ByteArray, ByteArray> = {
        KafkaConsumer<ByteArray, ByteArray>(consumerProperties())
    }

    fun consumerProperties() =
            Properties().apply {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
                put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup)
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer::class.java)
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer::class.java)
                put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false)
                put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5_000)
                put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 2.minutes.inMilliseconds.toInt())
                if (useSsl) {
                    put("security.protocol", "SSL")
                    put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, trustStore)
                    put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, trustStorePassword)
                    put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keyStore)
                    put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keyStorePassword)
                    put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, keyPassword)
                }
            }
}
