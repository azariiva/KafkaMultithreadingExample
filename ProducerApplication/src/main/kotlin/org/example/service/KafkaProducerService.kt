package org.example.service

import org.example.config.kafka.KafkaConfig.TOPIC_TEST_ONE
import org.example.model.TestEntity
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class KafkaProducerService(
    private val kafka: KafkaTemplate<String,Any>
) {
    @Scheduled(initialDelay = 0, fixedDelay = 500)
    fun poll() {
        kafka.send(TOPIC_TEST_ONE, TestEntity(id = null, value = Random.nextInt(100).toString()))
    }
}