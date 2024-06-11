package com.rahul.sdr.kafka.producer;

import com.rahul.sdr.kafka.generated.ProductAvro;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, ProductAvro> kafkaTemplate;

    public static final String TOPIC = "product_updates_topic";

    public void sendMessage(ProductAvro product) {
        ProducerRecord<String, ProductAvro> productRecord = new ProducerRecord<>(TOPIC, product.getId() + "", product);
        kafkaTemplate.send(productRecord);
    }
}