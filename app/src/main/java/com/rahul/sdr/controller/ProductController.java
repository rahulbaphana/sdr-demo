package com.rahul.sdr.controller;

import com.rahul.sdr.entity.Product;
import com.rahul.sdr.kafka.generated.ProductAvro;
import com.rahul.sdr.kafka.producer.KafkaProducerService;
import com.rahul.sdr.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductDao productDao;
    private final KafkaProducerService producerService;

    @PostMapping(value = "/products")
    public Product save(@RequestBody Product product) {
        return productDao.save(product);
    }

    @GetMapping(value = "/products")
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity findProduct(@PathVariable int id) {
        return ResponseEntity.ok(productDao.findProductById(id));
    }

    @DeleteMapping("/products/{id}")
    public String remove(@PathVariable int id) {
        return productDao.deleteProduct(id);
    }

    @PostMapping("/product/sendAvroMessage")
    public String sendMessageToKafkaTopic(@RequestBody Product product) {
        ProductAvro productAvro = new ProductAvro(product.getId(), product.getName(), product.getQuantity(), product.getPrice());
        producerService.sendMessage(productAvro);
        return "Message sent to Kafka topic product_updates_topic";
    }
}
