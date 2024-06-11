package com.rahul.sdr.kafka.producer;

import com.rahul.sdr.BaseIntegrationTest;
import com.rahul.sdr.RetryUtil;
import com.rahul.sdr.entity.Product;
import com.rahul.sdr.kafka.generated.ProductAvro;
import com.rahul.sdr.repository.ProductDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class KafkaProducerServiceTest extends BaseIntegrationTest {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ConsumerFactory consumerFactory;

    @Autowired
    private ProductDao productDao;

    @Test
    void testProducerUntilRedisCacheIsUpdated() {
        final AtomicInteger counter = new AtomicInteger(0);
        ProductAvro doveSoap = new ProductAvro(7, "Dove Soap", 11, 52L);
        productDao.deleteProduct(doveSoap.getId());

        kafkaProducerService.sendMessage(doveSoap);

        Product result = RetryUtil.retryUntil(
                () -> {
                    int count = counter.incrementAndGet();
                    if (count < 5) {
                        return null;
                    } else {
                        return productDao.findProductById(doveSoap.getId());
                    }
                },
                Objects::nonNull,
                Duration.ofSeconds(30),
                Duration.ofMillis(500)
        );
        Assertions.assertEquals(doveSoap.getId(), result.getId());
        Assertions.assertEquals(doveSoap.getName(), result.getName());
        Assertions.assertEquals(doveSoap.getQuantity(), result.getQuantity());
        Assertions.assertEquals(doveSoap.getPrice(), result.getPrice());
    }
}