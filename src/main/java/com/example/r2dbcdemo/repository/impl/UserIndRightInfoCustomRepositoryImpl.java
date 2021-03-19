package com.example.r2dbcdemo.repository.impl;

import com.example.r2dbcdemo.repository.UserIndRightInfoCustomRepository;
import com.example.r2dbcdemo.repository.entity.UserIndRightInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import static org.springframework.data.relational.core.query.Criteria.*;
import static org.springframework.data.relational.core.query.Query.*;

import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Transactional
public class UserIndRightInfoCustomRepositoryImpl implements UserIndRightInfoCustomRepository {

    private final R2dbcEntityTemplate template;

    @Override
    public Mono<UserIndRightInfo> findById(String id, String indInfoGbCd) {
        return template.select(UserIndRightInfo.class)
                .matching(query(where("user_id").is(id).and(where("ind_info_gb_cd").is(indInfoGbCd))))
                .one();
    }
}
