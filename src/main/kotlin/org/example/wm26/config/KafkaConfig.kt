package org.example.wm26.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {

    @Bean
    fun newTopic(): NewTopic {
        return TopicBuilder.name("wm26-match-results")
            .partitions(1)
            .replicas(1)
            .build()
    }

}
