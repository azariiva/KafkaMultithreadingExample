package org.example.config.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import kotlin.NotImplementedError;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;

import java.lang.reflect.Type;
import java.util.Map;

import static com.fasterxml.jackson.module.kotlin.ExtensionsKt.jacksonObjectMapper;

/**
 * @see <a href="https://medium.com/@agvillamizar/implementing-custom-serdes-for-java-objects-using-json-serializer-and-deserializer-in-kafka-streams-d794b66e7c03">medium</a>
 */
public class JsonDeserializer<T> implements Deserializer<T> {
    private final ObjectMapper objectMapper = jacksonObjectMapper();

    private final Class<T> destinationClass;
    private Type reflectionTypeToken;

    public JsonDeserializer(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }

    public JsonDeserializer(Type reflectionTypeToken) {
        throw new NotImplementedError(); // TODO
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        return null;
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] bytes) {
        if (bytes == null)
            return null;

        try {
            return objectMapper.readValue(bytes, destinationClass);
        } catch (Exception e) {
            throw new SerializationException("Error deserializing message", e);
        }
    }

    @Override
    public void close() {
        // nothing to do
    }
}