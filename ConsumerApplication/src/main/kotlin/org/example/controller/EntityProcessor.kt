package org.example.controller

import org.apache.ignite.client.ClientCache
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Printed
import org.example.config.kafka.CustomSerdes
import org.example.config.kafka.KafkaConfig.TOPIC_TEST_ONE
import org.example.config.kafka.KafkaConfig.TOPIC_TEST_TWO
import org.example.model.TestEntity
import org.example.service.TestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ThreadPoolExecutor

/**
 * @see <a href="https://developpaper.com/using-multithreading-to-increase-kafka-consumption/">developpaper</a>
 * @see <a href="https://www.baeldung.com/spring-boot-kafka-streams">baeldung</a>
 */
@Component
class EntityProcessor(
    private val kafka: KafkaTemplate<String, Any>,
    private val service: TestService,
    private val executor: ThreadPoolExecutor,
    private val testEntityCache: ClientCache<UUID, TestEntity>
) {
    @Autowired
    fun buildPipeline(
        streamsBuilder: StreamsBuilder
    ) {
        val input = streamsBuilder.stream(
            TOPIC_TEST_ONE,
            Consumed.with(Serdes.String(), CustomSerdes.TestEntity())
        )
        input
            .foreach { _, entity ->
                executor.submit {
                    val entityWithId = entity.copy(id = entity.generateId())
                    when (testEntityCache.get(entityWithId.id)) {
                        null -> { // save new entity
                            service.saveEntity(entityWithId)
                            testEntityCache.put(entityWithId.id, entityWithId)
                        }
                        entityWithId -> Unit // do nothing
                        else -> { // update entity
                            service.saveEntity(entityWithId)
                            testEntityCache.put(entityWithId.id, entityWithId)
                        }
                    }
                    kafka.send(TOPIC_TEST_TWO, entityWithId)
                }
            }
        input.print(Printed.toSysOut()) // TODO: remove
    }
}