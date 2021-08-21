package com.restfultutorial01.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> root(ProductHandler productHandler){
        return RouterFunctions.route()
                .GET("/product", productHandler::findAll)
                .GET("/product/{productId}", productHandler::findById)
                .POST("/product", productHandler::save)
                .PUT("/product/{productId}", productHandler::update)
                .DELETE("/product/{productId}", productHandler::deleteById)
                .build();
    }
}
