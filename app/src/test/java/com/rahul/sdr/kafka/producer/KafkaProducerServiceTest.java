package com.rahul.sdr.kafka.producer;

import com.rahul.sdr.BaseIntegrationTest;
import com.rahul.sdr.kafka.generated.ProductAvro;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SpringBootTest
class KafkaProducerServiceTest extends BaseIntegrationTest {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private ConsumerFactory consumerFactory;

    @Autowired
    private KafkaTemplate<String, ProductAvro> kafkaTemplate;

    @Autowired
    private ConcurrentKafkaListenerContainerFactory<String, ProductAvro> kafkaListenerContainerFactory;

    @Test
    @Ignore
    void testProducer() {
        ProductAvro doveSoap = new ProductAvro(7, "Dove Soap", 11, 52L);
        Map props = consumerFactory.getConfigurationProperties();
        KafkaConsumer consumer = new KafkaConsumer(props);
        consumer.subscribe(List.of(KafkaProducerService.TOPIC));

        kafkaProducerService.sendMessage(doveSoap);

        Object next = consumer.poll(Duration.ofSeconds(30)).iterator().next();
        Assertions.assertNotNull(next);
    }
}