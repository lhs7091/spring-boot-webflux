package com.restfultutorial01.repository;

import com.restfultutorial01.domain.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
