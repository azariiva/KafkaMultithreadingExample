package org.example.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {
    public static final String TOPIC_TEST_ONE = "test.one";

    @Bean
    public NewTopic testOneTopic() {
        return TopicBuilder
                .name(TOPIC_TEST_ONE)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public ProducerFactory<String, ?> producerFactory(KafkaProperties kafkaProperties) {
        var producerProperties = kafkaProperties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }

    @Bean
    public KafkaTemplate<String, ?> kafkaTemplate(ProducerFactory<String, ?> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
