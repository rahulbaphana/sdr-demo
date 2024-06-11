package com.rahul.sdr;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.Properties;

@SpringBootTest
public abstract class BaseIntegrationTest {
    public static final Network NETWORK = Network.newNetwork();

    @Container
    public static GenericContainer REDIS = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
            .withNetworkAliases("redis-standalone")
            .withExposedPorts(6379);


    @Container
    private static final KafkaContainer KAFKA_CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.2"))
                    .withNetworkAliases("kafka")
                    .withEnv("KAFKA_CREATE_TOPICS", "product_updates_topic:1:1")
                    .withExposedPorts(9093)
                    .withEmbeddedZookeeper()
                    .withNetwork(NETWORK);

    @Container
    private static GenericContainer<?> SCHEMA_REGISTRY;

    @BeforeAll
    public static void beforeAll() {
        if (!REDIS.isRunning()) {
            REDIS.start();
        }

        if (!KAFKA_CONTAINER.isRunning()) {
            KAFKA_CONTAINER.start();
        }

        SCHEMA_REGISTRY = new GenericContainer<>(DockerImageName.parse("confluentinc/cp-schema-registry:7.5.2"))
                        .withNetwork(NETWORK)
                        .withExposedPorts(8081)
                        .withNetworkAliases("schema-registry")
                        .withEnv("SCHEMA_REGISTRY_HOST_NAME", "schema-registry")
                        .withEnv("SCHEMA_REGISTRY_LISTENERS", "http://0.0.0.0:8081")
                        .withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", "PLAINTEXT://" + KAFKA_CONTAINER.getNetworkAliases().get(0) + ":9092")
                        .waitingFor(Wait.forHttp("/subjects").forStatusCode(200));

        if (!SCHEMA_REGISTRY.isRunning()) {
            SCHEMA_REGISTRY.start();
        }

        createTopic();
    }

    @DynamicPropertySource
    public static void springBootAppProperties(DynamicPropertyRegistry registry) {
        // Redis configuration
        registry.add("spring.redis.host", REDIS::getHost);
        registry.add("spring.redis.port", REDIS::getFirstMappedPort);

        // Kafka configuration
        registry.add("spring.kafka.consumer.bootstrap-servers", () -> KAFKA_CONTAINER.getHost() + ":2181");
        registry.add("spring.kafka.producer.bootstrap-servers", () -> KAFKA_CONTAINER.getHost() + ":2181");
        registry.add("spring.kafka.consumer.group-id", () -> "sdr-demo");
        registry.add("spring.kafka.properties.schema.registry.url", () -> "http://" + SCHEMA_REGISTRY.getHost() + ":" + SCHEMA_REGISTRY.getExposedPorts().get(0));
    }

    @AfterAll
    public static void tearDown() {
        REDIS.stop();
        KAFKA_CONTAINER.stop();
        SCHEMA_REGISTRY.stop();
    }

    private static void createTopic() {
        Properties config = new Properties();
        config.put("bootstrap.servers", KAFKA_CONTAINER.getBootstrapServers());
        AdminClient admin = AdminClient.create(config);
        try {
            admin.createTopics(Collections.singletonList(new NewTopic("product_updates_topic", 1, (short) 1))).all().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        admin.close();
    }
}
