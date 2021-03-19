package com.example.r2dbcdemo.repository;

import com.example.r2dbcdemo.repository.entity.User;
import reactor.core.publisher.Mono;

public interface UserCustomRepository {

    Mono<User> updateByTemplate(User updatedUser);

    Mono<User> updateByDatabaseClient(User updatedUser);
}
