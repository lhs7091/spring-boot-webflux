package com.restfultutorial01.controller;

import com.restfultutorial01.domain.Product;
import com.restfultutorial01.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Log4j2
public class ProductHandler {

    private final ProductService productService;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest){
        Flux<Product> productFlux = productService.findAll();
        return ServerResponse.ok().body(productFlux, Product.class);
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest){
        String productId = serverRequest.pathVariable("productId");

        return productService.findById(productId)
                .flatMap(product -> ServerResponse.ok().bodyValue(product))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest){
        Mono<Product> productMono = serverRequest.bodyToMono(Product.class)
                .flatMap(productService::save);

        return ServerResponse.status(HttpStatus.CREATED)
                .body(productMono, Product.class);
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest){
        String productId = serverRequest.pathVariable("productId");

        return productService.findById(productId)
                .flatMap(product -> {
                   Mono<Product> updated = serverRequest.bodyToMono(Product.class)
                           .flatMap(p->{
                               if (p.getId() != null) {
                                   productService.save(p);
                                   return Mono.just(p).log();
                               }
                               return Mono.empty();

                           });

                   return ServerResponse.ok().body(updated, Product.class);

                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest){
        String productId = serverRequest.pathVariable("productId");

        return productService.findById(productId)
                .flatMap(product ->
                    productService.delete(product.getId())
                            .then(ServerResponse.ok().bodyValue(product)))
                            .switchIfEmpty(ServerResponse.notFound().build());

    }
}
