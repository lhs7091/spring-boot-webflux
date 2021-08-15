package com.restfultutorial01.config;

import com.restfultutorial01.domain.Product;
import com.restfultutorial01.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Log4j2
public class DBInit implements ApplicationListener<ApplicationReadyEvent> {

    private final ProductRepository productRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        productRepository.deleteAll().thenMany(
                Flux.just("aaa", "bbb", "ccc", "ddd").map(
                        name->new Product(UUID.randomUUID().toString(), name, ThreadLocalRandom.current().nextDouble(1000, 5000))
                ).flatMap(productRepository::save)
        )
                .thenMany(productRepository.findAll())
                .subscribe(product -> log.info("Saved product {}", product));
    }
}
