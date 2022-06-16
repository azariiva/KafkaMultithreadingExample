package org.example.controller

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Printed
import org.example.config.kafka.CustomSerdes
import org.example.config.kafka.KafkaConfig.TOPIC_TEST_ONE
import org.example.config.kafka.KafkaConfig.TOPIC_TEST_TWO
import org.example.service.TestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * @see <a href="https://developpaper.com/using-multithreading-to-increase-kafka-consumption/">developpaper</a>
 */
@Component
class EntityProcessor(
    private val kafka: KafkaTemplate<String, Any>,
    private val service: TestService,
) {
    private val executor = ThreadPoolExecutor(
        5,
        20,
        10,
        TimeUnit.SECONDS,
        SynchronousQueue(),
        ThreadPoolExecutor.CallerRunsPolicy()
    )

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
                    kafka.send(TOPIC_TEST_TWO, service.saveOrGetId(entity))
                }
            }
        input.print(Printed.toSysOut()) // TODO: remove
    }
}