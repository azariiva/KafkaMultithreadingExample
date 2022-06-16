package application

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams

@SpringBootApplication(scanBasePackages = ["org.example"])
@EnableKafka
@EnableKafkaStreams
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}