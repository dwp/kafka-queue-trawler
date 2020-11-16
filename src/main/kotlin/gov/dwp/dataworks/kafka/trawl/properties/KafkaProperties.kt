package gov.dwp.dataworks.kafka.trawl.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "kafka")
data class KafkaProperties(var bootstrapServers: String = "",
                           var consumerGroup: String = "topic-trawler",
                           var useSsl: Boolean = false,
                           var trustStore: String = "",
                           var trustStorePassword: String = "",
                           var keyStore: String = "",
                           var keyStorePassword: String = "",
                           var keyPassword: String = "",
                           var initialOffset: Long = 0) {
    @Bean
    fun bootstrapServers() = bootstrapServers

    @Bean
    fun consumerGroup() = consumerGroup

    @Bean
    fun trustStore() = trustStore

    @Bean
    fun trustStorePassword() = trustStorePassword

    @Bean
    fun keyStore() = keyStore

    @Bean
    fun keyStorePassword() = keyStorePassword

    @Bean
    fun keyPassword() = keyPassword

    @Bean
    fun useSsl() = useSsl

    @Bean
    fun initialOffset() = initialOffset
}
