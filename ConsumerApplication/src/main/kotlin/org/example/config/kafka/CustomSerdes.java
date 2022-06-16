package org.example.config.kafka;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.example.model.TestEntity;

public final class CustomSerdes {
    private CustomSerdes() {
    }

    public static Serde<TestEntity> TestEntity() {
        JsonSerializer<TestEntity> serializer = new JsonSerializer<>();
        JsonDeserializer<TestEntity> deserializer = new JsonDeserializer<>(TestEntity.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}
