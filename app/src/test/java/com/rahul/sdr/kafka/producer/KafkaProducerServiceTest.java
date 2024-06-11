package com.rahul.sdr.kafka.producer;

import com.rahul.sdr.BaseIntegrationTest;
import com.rahul.sdr.entity.Product;
import com.rahul.sdr.kafka.generated.ProductAvro;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;

@SpringBootTest
class KafkaProducerServiceTest extends BaseIntegrationTest {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ConsumerFactory consumerFactory;

    @Test
    void testProducerUntilRedisCacheIsUpdated() {
        // Given
        final AtomicInteger counter = new AtomicInteger(0);
        ProductAvro doveSoap = new ProductAvro(7, "Dove Soap", 11, 52L);
        Assertions.assertNull(productDao.findProductById(doveSoap.getId()));

        // When
        kafkaProducerService.sendMessage(doveSoap);

        // Then
        await().atMost(TIMEOUT_30_SECONDS).until(() -> productDao.findProductById(doveSoap.getId()) != null);
        Product result = productDao.findProductById(doveSoap.getId());
        Assertions.assertEquals(doveSoap.getId(), result.getId());
        Assertions.assertEquals(doveSoap.getName(), result.getName());
        Assertions.assertEquals(doveSoap.getQuantity(), result.getQuantity());
        Assertions.assertEquals(doveSoap.getPrice(), result.getPrice());
    }
}