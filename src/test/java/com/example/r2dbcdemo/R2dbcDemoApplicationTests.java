package com.example.r2dbcdemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

//@SpringBootTest
class R2dbcDemoApplicationTests {

    @Test
    void test() {
        Flux<Integer> generator = Flux.generate(AtomicInteger::new, (atomicInteger, synchronousSink) -> {
            synchronousSink.next(atomicInteger.getAndIncrement());
            return atomicInteger;
        });
        Mono<Integer> next = generator.next();
    }

}
