package com.rahul.sdr.kafka.consumer;

import com.rahul.sdr.entity.Product;
import com.rahul.sdr.kafka.generated.ProductAvro;
import com.rahul.sdr.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final ProductDao productDao;

    @KafkaListener(topics = "product_updates_topic", groupId = "sdr-demo")
    public void consume(ConsumerRecord<String, ProductAvro> productRecord) {
        Product product = new Product(productRecord.value().getId(), productRecord.value().getName(), productRecord.value().getQuantity(), productRecord.value().getPrice());
        productDao.save(product);
        System.out.println("Received product: " + productRecord.value());
    }
}
