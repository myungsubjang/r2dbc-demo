package com.example.r2dbcdemo.repository;

import com.example.r2dbcdemo.repository.entity.UserIndRightInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserIndRightInfoRepository extends ReactiveCrudRepository<UserIndRightInfo, String>, UserIndRightInfoCustomRepository {

    Flux<UserIndRightInfo> findByUserId(String id);

    @Override
    @Deprecated
    Mono<UserIndRightInfo> findById(String s);
}
