package com.restfultutorial01.controller;

import com.restfultutorial01.domain.Product;
import com.restfultutorial01.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
@WebFluxTest
@ContextConfiguration(classes = {ProductHandler.class, ProductRouter.class})
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void findAllTest() {
        Mockito.when(productService.findAll())
                .thenReturn(
                        Flux.just(
                                new Product(UUID.randomUUID().toString(), "aaa", 2999.99),
                                new Product(UUID.randomUUID().toString(), "bbb", 1111.11),
                                new Product(UUID.randomUUID().toString(), "ccc", 3333.22)
                        )
                );

        webTestClient.get()
                .uri("/product")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].name").isEqualTo("aaa")
                .jsonPath("$[1].price").isEqualTo(1111.11);

        log.info("Test find all Product");
    }

    @Test
    void saveTest() {
        Product product = newProduct();

        Mockito.when(productService.save(Mockito.any(Product.class)))
                .thenReturn(
                        Mono.just(product)
                );

        webTestClient.post()
                .uri("/product")
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(product.getName());
    }

    @Test
    void findByIdTest() {
        Product product = newProduct();

        Mockito.when(productService.findById(product.getId()))
                .thenReturn(
                        Mono.just(product)
                );

        webTestClient.get()
                .uri("/product/"+product.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(product.getName())
                .jsonPath("$.price").isEqualTo(product.getPrice());

        log.info("Test find product by id");
    }

    @Test
    void updateTest() {
        Product product = newProduct();

        Mockito.when(productService.findById(product.getId()))
                .thenReturn(Mono.just(product));
        Mockito.when(productService.save(product))
                .thenReturn(Mono.just(product));

        webTestClient.put()
                .uri("/product/"+product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(product), Product.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("aaa");

        log.info("Test update product");
    }

    @Test
    void deleteByIdTest() {
        Product product = newProduct();
        Mockito.when(productService.findById(product.getId()))
                .thenReturn(
                        Mono.just(product)
                );
        Mockito.when(productService.delete(product.getId()))
                .thenReturn(
                        Mono.just(product)
                );

        webTestClient.delete()
                .uri("/product/"+product.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("aaa");

        log.info("Test delete by id");
    }

    Product newProduct(){
        return new Product(UUID.randomUUID().toString(), "aaa", 1111.11);
    }
}