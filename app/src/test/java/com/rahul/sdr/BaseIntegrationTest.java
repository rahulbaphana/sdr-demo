package com.rahul.sdr;

import com.rahul.sdr.repository.ProductDao;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@SpringBootTest
@EnableKafka
@EmbeddedKafka(
        partitions = 1,
        topics = {"product_updates_topic"},
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:29092",
                "port=29092"
        }
)
public abstract class BaseIntegrationTest {
    public static final Duration TIMEOUT_30_SECONDS = Duration.ofSeconds(30);

    @Autowired
    protected EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    protected DynamicPropertyRegistry registry;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    protected ProductDao productDao;

    protected MockSchemaRegistryClient schemaRegistryClient;

    @Container
    public static GenericContainer REDIS = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
            .withNetworkAliases("redis-standalone")
            .withExposedPorts(6379);

    static {
        if (!REDIS.isRunning()) {
            REDIS.start();
        }
    }

    @BeforeEach
    void setUp() {
        this.schemaRegistryClient = new MockSchemaRegistryClient();
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @DynamicPropertySource
    public  static void springBootAppProperties(DynamicPropertyRegistry registry) {
        // Redis configuration
        registry.add("spring.redis.host", REDIS::getHost);
        registry.add("spring.redis.port", REDIS::getFirstMappedPort);

        // Kafka configuration
        registry.add("spring.kafka.consumer.group-id", () -> "sdr-demo");
        registry.add("spring.kafka.properties.schema.registry.url", () -> "mock://localhost");
    }
}
