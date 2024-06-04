package com.rahul.sdr.controller;

import com.rahul.sdr.entity.Product;
import com.rahul.sdr.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductDao productDao;

    @PostMapping(value = "/products")
    public Product save(@RequestBody Product product) {
        return productDao.save(product);
    }

    @GetMapping(value = "/products")
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @GetMapping("/products/{id}")
    public Product findProduct(@PathVariable int id) {
        return productDao.findProductById(id);
    }

    @DeleteMapping("/products/{id}")
    public String remove(@PathVariable int id) {
        return productDao.deleteProduct(id);
    }
}
