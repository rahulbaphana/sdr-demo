package com.rahul.sdr.repository;

import com.rahul.sdr.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductDao {
    private static final String HASH_KEY = "Product";

    private final RedisTemplate redisTemplate;

    public Product save(Product product) {
        getHashOperations().put(HASH_KEY, "" + product.getId(), product);
        return product;
    }

    public List<Product> findAll() {
        return getHashOperations().values(HASH_KEY);
    }

    public Product findProductById(int id) {
        return (Product) getHashOperations().get(HASH_KEY, "" + id);
    }

    public String deleteProduct(int id) {
        getHashOperations().delete(HASH_KEY, "" + id);
        return "Product removed!";
    }

    private HashOperations getHashOperations() {
        return redisTemplate.opsForHash();
    }
}
