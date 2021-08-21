package com.restfultutorial01.controller;

import com.restfultutorial01.domain.Product;
import com.restfultutorial01.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Flux<Product> findAll(){
        return productService.findAll().log();
    }

    @PostMapping
    public Mono<ResponseEntity<Product>> save(@RequestBody Product product){
        return productService.save(product)
                .map(savedProduct -> new ResponseEntity<>(savedProduct, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST))
                .log();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> findById(@PathVariable(value = "id") String productId){
        return productService.findById(productId)
                .map(p -> ResponseEntity.ok(p))
                .defaultIfEmpty(ResponseEntity.notFound().build()).log();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Product>> update(@PathVariable(value = "id") String productId, @RequestBody Product product){
        return productService.findById(productId)
                .flatMap(savedProduct -> {
                    savedProduct.updateProduct(product.getName(), product.getPrice());
                    return productService.save(savedProduct);
                })
                .map(updatedProduct -> new ResponseEntity<>(updatedProduct, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND)).log();
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Product>> deleteById(@PathVariable(value = "id") String productId){
        return productService.findById(productId)
                .flatMap(p -> productService.delete(productId)
                        .thenReturn(ResponseEntity.ok(p)))
                .defaultIfEmpty(ResponseEntity.notFound().build()).log();
    }
}
