package com.inventory.config

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaAdmin

@Configuration
@EnableKafka
class KafkaTopicConfig {
    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs = hashMapOf<String, Any>(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092"
        )

        return KafkaAdmin(configs)
    }

    @Bean
    fun inventoryEventsTopic(): NewTopic {
        return TopicBuilder.name("inventory-events")
            .partitions(3)
            .replicas(1)
            .build()
    }
}