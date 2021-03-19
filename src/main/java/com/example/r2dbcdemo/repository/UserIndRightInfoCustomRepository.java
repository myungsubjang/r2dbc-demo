package com.example.r2dbcdemo.repository;

import com.example.r2dbcdemo.repository.entity.UserIndRightInfo;
import reactor.core.publisher.Mono;

public interface UserIndRightInfoCustomRepository {

    Mono<UserIndRightInfo> findById(String id, String indInfoGbCd);
}
