package com.example.r2dbcdemo.repository;

import com.example.r2dbcdemo.repository.dto.PersonalInfo;
import com.example.r2dbcdemo.repository.dto.Telephone;
import com.example.r2dbcdemo.repository.entity.User;
import com.example.r2dbcdemo.repository.projection.Cellphone;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface UserRepository extends ReactiveCrudRepository<User, String>, UserCustomRepository {

    @Query("UPDATE ST_USER_BASE SET USER_NM = :name, EMAIL_ADDR = :emailAddress WHERE USER_ID = :id")
    Mono<Void> updateUserInfoBySql(String id, String name, String emailAddress);

    Flux<User> findUsersBySysModDtmBetween(LocalDateTime startDay, LocalDateTime endDay);

    Mono<Cellphone> findCellphoneById(String id);

    Mono<Telephone> findTelephoneById(String id);

    Mono<PersonalInfo> findPersonalInfoById(String id);

    <T> Mono<T> findById(String id, Class<T> type);

    Flux<User> findAllBy(Pageable pageable);


}
