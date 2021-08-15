package com.restfultutorial01.service;

import com.restfultutorial01.domain.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@DataMongoTest
@Import(ProductServiceImpl.class)
class ProductServiceTest {

    @Autowired
    ProductService productService;

    Product createProduct(){
        return new Product(UUID.randomUUID().toString(), "eee", ThreadLocalRandom.current().nextDouble(1000, 5000));
    }

    @Test
    void saveTest() {
        // given
        Product product = createProduct();

        // when
        Mono<Product> productMono = productService.save(product);

        // then
        StepVerifier.create(productMono)
                .expectNextMatches(p -> StringUtils.hasText(product.getName()))
                .verifyComplete();
    }

    @Test
    void updateTest(){
        // given
        Product product = createProduct();
        Mono<Product> productMono = productService.save(product);
        Product savedProduct = productMono.block();
        savedProduct.updateProduct("aaa", null);

        // when
        Mono<Product> updateProduct = productService.save(savedProduct);

        // then
        StepVerifier.create(updateProduct)
                .expectNextMatches(p -> p.getName().equals("aaa"))
                .verifyComplete();

        assertEquals(savedProduct.getName(), updateProduct.block().getName());

    }

    @Test
    void deleteTest() {
        // given
        Mono<Product> productMono = productService.save(createProduct());

        // when
        Mono<Product> deleteProduct = productService.delete(productMono.block().getId());

        // then
        StepVerifier.create(deleteProduct)
                .expectNextMatches(p -> p.getName().equals("eee"))
                .verifyComplete();
    }

    @Test
    void findByIdTest() {
        Mono<Product> productMono = productService.save(createProduct())
                .flatMap(p -> productService.findById(p.getId()));

        StepVerifier.create(productMono)
                .expectNextMatches(p -> p.getName().equals("eee"))
                .verifyComplete();
    }
}