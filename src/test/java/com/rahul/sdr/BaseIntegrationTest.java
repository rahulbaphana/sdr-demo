package com.rahul.sdr;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Container
    public static GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
            .withExposedPorts(6379);

    static
    {
        if (!redis.isRunning()) {
            redis.start();
        }
    }

    @DynamicPropertySource
    public static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
    }
}
