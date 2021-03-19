package com.example.r2dbcdemo.config.auditor;

import com.example.r2dbcdemo.util.Util;
import org.springframework.data.domain.ReactiveAuditorAware;
import reactor.core.publisher.Mono;

public class SampleAuditorAware implements ReactiveAuditorAware<String> {
    @Override
    public Mono<String> getCurrentAuditor() {

        return Mono.just(Util.getSessionId());
    }
}
