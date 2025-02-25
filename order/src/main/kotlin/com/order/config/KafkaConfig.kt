package com.order.config

import com.order.dto.event.OrderCreatedEvent
import com.order.dto.event.inventory.InventoryEvent
import com.order.dto.event.inventory.InventoryRejectedEvent
import com.order.dto.event.inventory.InventoryReservedEvent
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
@EnableKafka
class KafkaConfig {

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        val configProps = hashMapOf<String, Any>(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to (System.getenv("KAFKA_BOOTSTRAP_SERVERS") ?: "kafka:9092"),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG to true,
            ProducerConfig.TRANSACTIONAL_ID_CONFIG to "order-transaction-"
        )

        val factory = DefaultKafkaProducerFactory<String, Any>(configProps)
        factory.setTransactionIdPrefix("order-transaction-")

        return factory
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(producerFactory())
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, InventoryEvent> {
        val configProps = hashMapOf<String, Any>(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to (System.getenv("KAFKA_BOOTSTRAP_SERVERS") ?: "kafka:9092"),
            ConsumerConfig.GROUP_ID_CONFIG to "order-group",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.ISOLATION_LEVEL_CONFIG to "read_committed",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java,
            "spring.deserializer.key.delegate.class" to StringDeserializer::class.java,
            "spring.deserializer.value.delegate.class" to JsonDeserializer::class.java,
            JsonDeserializer.TRUSTED_PACKAGES to "*",
            "spring.json.use.type.headers" to true,
            "spring.json.type.mapping" to "com.inventory.dto.event.ordered.OrderCreatedEvent:" +
                    OrderCreatedEvent::class.java.name + "," +
                    "com.inventory.dto.event.reserved.InventoryReservedEvent:" +
                    InventoryReservedEvent::class.java.name + "," +
                    "com.inventory.dto.event.rejected.InventoryRejectedEvent:" +
                    InventoryRejectedEvent::class.java.name
        )
        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, InventoryEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, InventoryEvent>()
        factory.consumerFactory = consumerFactory()

        return factory
    }
}