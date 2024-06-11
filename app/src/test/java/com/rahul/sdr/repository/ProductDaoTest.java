package com.rahul.sdr.repository;

import com.rahul.sdr.BaseIntegrationTest;
import com.rahul.sdr.entity.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductDaoTest extends BaseIntegrationTest {

    @Test
    void saveProduct_shouldSaveProductToRedis() {
        // Given
        Product product = new Product(1, "Product1", 10, 100L);

        // When
        Product savedProduct = productDao.save(product);

        // Then
        assertNotNull(savedProduct);
        assertEquals(product.getId(), savedProduct.getId());
    }

    @Test
    void findProductById_shouldGetTheProduct_whenItExists() {
        // Given
        Product product = new Product(121, "Product1", 10, 100L);
        Product savedProduct = productDao.save(product);

        // When
        Product findProductResult = productDao.findProductById(savedProduct.getId());

        // Then
        assertNotNull(findProductResult);
        assertEquals(findProductResult, savedProduct);
    }
}