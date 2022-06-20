package org.example.config.ignite;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.client.SslMode;
import org.apache.ignite.configuration.BinaryConfiguration;
import org.apache.ignite.configuration.ClientConfiguration;
import org.example.model.TestEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.expiry.Duration;
import javax.cache.expiry.TouchedExpiryPolicy;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
public class IgniteConfig {
    private static final String TEST_ENTITY_CACHE = "test.entity.cache";

    @Value("${ignite.node}")
    private String igniteNode;

    @Bean
    public ClientCache<UUID, TestEntity> testEntityCache(IgniteClient igniteClient) {
        return igniteClient
                .getOrCreateCache(TEST_ENTITY_CACHE)
                .withExpirePolicy(
                        new TouchedExpiryPolicy(new Duration(TimeUnit.HOURS, 1))
                );
    }

    @Bean
    public IgniteClient igniteClient(ClientConfiguration igniteClientConfiguration) {
        return Ignition.startClient(igniteClientConfiguration);
    }

    @Bean
    public ClientConfiguration igniteClientConfiguration() {
        return new ClientConfiguration()
                .setAddresses(igniteNode)
                .setSslMode(SslMode.DISABLED)
                .setPartitionAwarenessEnabled(true)
                .setBinaryConfiguration(new BinaryConfiguration().setCompactFooter(true));
    }
}
