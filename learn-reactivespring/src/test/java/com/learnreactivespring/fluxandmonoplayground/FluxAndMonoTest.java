package com.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
//                .concatWith(Flux.error(new RuntimeException("error test")))
                .concatWith(Flux.just("After Error")) // Error 이후에는 동작을 실행하지 않음.
                .log();

        stringFlux.subscribe(System.out::println, (e)->System.err.println(e), ()->System.out.println("Completed"));
    }

    @Test
    void fluxTestElements_WithoutError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring boot")
                .expectNext("Reactive Spring")
                .verifyComplete();
    }

    @Test
    void fluxTestElements_WithError1(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("error test")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring boot")
                .expectNext("Reactive Spring")
//                .expectError(RuntimeException.class)
                .expectErrorMessage("error test")
                .verify();
    }

    @Test
    void fluxTestElements_WithError2(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("error test")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring boot", "Reactive Spring")
//                .expectError(RuntimeException.class)
                .expectErrorMessage("error test")
                .verify();
    }

    @Test
    void fluxTestElementsCount_WithError(){
        Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("error test")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
//                .expectError(RuntimeException.class)
                .expectErrorMessage("error test")
                .verify();
    }

    @Test
    void monoTest(){
        Mono<String> stringMono = Mono.just("Spring");

        StepVerifier.create(stringMono)
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    void monoTest_Error(){
        Mono<String> stringMono = Mono.just("Spring");

        StepVerifier.create(Mono.error(new RuntimeException("error occurred")))
                .expectError(RuntimeException.class)
                .verify();
    }
}
