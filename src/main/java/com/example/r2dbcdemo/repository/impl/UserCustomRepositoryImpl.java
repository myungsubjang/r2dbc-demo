package com.example.r2dbcdemo.repository.impl;

import com.example.r2dbcdemo.domain.OrgRole;
import com.example.r2dbcdemo.repository.UserCustomRepository;
import com.example.r2dbcdemo.repository.UserRepository;
import com.example.r2dbcdemo.repository.entity.User;
import com.example.r2dbcdemo.util.Util;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.function.Function;

@Transactional
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final R2dbcEntityTemplate template;
    private final DatabaseClient dbClient;
    private static final String KEY_COLUMN = "user_id";

    // auditing 안됨, convert 됨
    @Override
    public Mono<User> updateByTemplate(User updatedUser) {
        Assert.notNull(updatedUser, "Id can not be null!");
        return Mono.just(updatedUser)
                .map(this::setModTimeAndId)
                .map(this::setRegTimeAndId)
                .flatMap(this::updateByConstructor)
                .flatMap(result -> findUserByTemplate(updatedUser.getId()));
    }

    private User setModTimeAndId(User user) {
        // 불변으로?
        user.setSysModId(Util.getSessionId());
        user.setSysModDtm(LocalDateTime.now());
        return user;
    }

    private User setRegTimeAndId(User user) {
        user.setSysRegId(Util.getSessionId());
        user.setSysRegDtm(LocalDateTime.now());
        return user;
    }

    private Mono<Integer> updateByConstructor(User updatedUser) {
        return template.update(
                Query.query(Criteria.where(KEY_COLUMN).is(updatedUser.getId())),
                Update.update("user_nm", updatedUser.getName())
                    .set("email_addr", updatedUser.getEmailAddr())
                    .set("org_rol_cd", OrgRole.MEMBER)
                    .set("sys_mod_dtm", updatedUser.getSysModDtm())
                    .set("sys_mod_id", updatedUser.getSysModId())
                    .set("sys_reg_dtm", updatedUser.getSysRegDtm())
                    .set("sys_reg_id", updatedUser.getSysRegId()),
                User.class
        );
    }

    private Mono<Integer> updateByFluent(User updatedUser) {
        return template.update(User.class)
                .matching(Query.query(Criteria.where(KEY_COLUMN).is(updatedUser.getId())))
                .apply(
                    Update.update("user_nm", updatedUser.getName())
                        .set("email_addr", updatedUser.getEmailAddr())
                        .set("org_rol_cd", OrgRole.MEMBER)
                );
    }

    private Mono<User> findUserByTemplate(String id) {
        return template.select(User.class)
                .matching(Query.query(Criteria.where("user_id").is(id)))
                .one();
    }

    //auditing 안됨 convert 안됨
    @Override
    public Mono<User> updateByDatabaseClient(User updatedUser) {
        return dbClient.sql("UPDATE ST_USER_BASE SET USER_NM = :name, EMAIL_ADDR = :emailAddr WHERE USER_ID = :id")
                .bind("name", updatedUser.getName())
                .bind("id", updatedUser.getId())
                .bind("emailAddr", updatedUser.getEmailAddr())
//                .bind("orgRole", OrgRole.MEMBER) // writing converter 안됨.
                .fetch()
                .rowsUpdated()
                .flatMap(result -> findUserByDatabaseClient(updatedUser.getId()));
    }

    private Mono<User> findUserByDatabaseClient(String userId) {
        final Function<Row, User> MAPPING_FUNCTION = row -> {
            User user = new User();
            user.setId(row.get("user_id", String.class));
            user.setEmailAddr(row.get("email_addr", String.class));
//            user.setOrgRole(row.get("org_rol_cd", OrgRole.class)); //reading converter 안됨
            return user;
        };
        return dbClient.sql("SELECT * FROM ST_USER_BASE WHERE USER_ID = :id")
                .bind("id", userId)
                .map(MAPPING_FUNCTION)
                .one();
    }

}
