package org.example.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.kafka.streams.StreamsConfig.*;

@Configuration
public class KafkaConfig {
    public static final String TOPIC_TEST_ONE = "test.one";
    public static final String TOPIC_TEST_TWO = "test.two";

    @Bean
    public NewTopic testOneTopic() {
        return TopicBuilder
                .name(TOPIC_TEST_ONE)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic testTwoTopic() {
        return TopicBuilder
                .name(TOPIC_TEST_TWO)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    ProducerFactory<String, ?> producerFactory(KafkaProperties kafkaProperties) {
        var producerProperties = kafkaProperties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }

    @Bean
    KafkaTemplate<String, ?> kafkaTemplate(ProducerFactory<String, ?> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    ConsumerFactory<?, ?> kafkaConsumerFactory(KafkaProperties properties) {
        var consumerProperties = properties.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(consumerProperties);
    }

    @Bean
    KafkaListenerContainerFactory<?> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(8);
        return factory;
    }

    @Bean
    ThreadPoolExecutor kafkaThreadPoolExecutor() {
        return new ThreadPoolExecutor(
                5,
                20,
                10,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kafkaStreamsConfiguration(
            @Value("${spring.kafka.streams.bootstrap-servers}") String bootstrapServers,
            @Value("${spring.kafka.streams.application-id}") String applicationId
    ) {
        Map<String, Object> props = Map.of(
                APPLICATION_ID_CONFIG, applicationId,
                BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName(),
                DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName()
        );
        return new KafkaStreamsConfiguration(props);
    }
}
