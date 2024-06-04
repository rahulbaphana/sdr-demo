package com.rahul.sdr.controller;

import com.rahul.sdr.entity.Product;
import com.rahul.sdr.repository.ProductDao;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductDao productDao;

    @PostMapping
    public Product save(@RequestBody Product product) {
        return productDao.save(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    @GetMapping("/{id}")
    public Product findProduct(@PathVariable int id) {
        return productDao.findProductById(id);
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable int id) {
        return productDao.deleteProduct(id);
    }
}
