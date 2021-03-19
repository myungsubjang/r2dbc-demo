package com.example.r2dbcdemo.handler;

import com.example.r2dbcdemo.domain.OrgRole;
import com.example.r2dbcdemo.domain.UserDetail;
import com.example.r2dbcdemo.repository.UserIndRightInfoRepository;
import com.example.r2dbcdemo.repository.UserRepository;
import com.example.r2dbcdemo.repository.dto.PersonalInfo;
import com.example.r2dbcdemo.repository.dto.Telephone;
import com.example.r2dbcdemo.repository.entity.User;
import com.example.r2dbcdemo.repository.entity.UserIndRightInfo;
import com.example.r2dbcdemo.repository.projection.Cellphone;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class SampleHandler {

    private final UserRepository userRepository;
    private final UserIndRightInfoRepository indRightInfoRepository;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return userRepository.findAll()
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @Transactional
    public Mono<ServerResponse> createUser(ServerRequest request) {

        return userRepository.findById("testtest")
                .map(this::createNewUser)
                .flatMap(userRepository::save)
                .zipWith(indRightInfoRepository.findByUserId("testtest")
                        .map(this::createNewUserIndRightInfo)
                        .flatMap(indRightInfoRepository::save)
                        //transaction test를 위한 코드
//                        .map(userIndRightInfo -> {
//                            if ("02".equals(userIndRightInfo.getIndInfoGbCd())) throw new RuntimeException("exception!");
//                            return userIndRightInfo;
//                        })
                        .collectList()
                        , User::withUserIndRightInfoList
                ).flatMap(ServerResponse.ok()::bodyValue);
    }

    private User createNewUser(User user) {
        user.setId("createdUser");
        user.setOrgRole(OrgRole.MEMBER);
        user.setSysRegDtm(null);
        user.setSysRegId(null);
        user.setSysModId(null);
        user.setNewUser(true);
        return user;
    }

    private UserIndRightInfo createNewUserIndRightInfo(UserIndRightInfo userIndRightInfo) {
        userIndRightInfo.setUserId("createdUser");
        userIndRightInfo.setNewIndRightInfo(true);
        userIndRightInfo.setSysRegId(null);
        userIndRightInfo.setSysRegDtm(null);
        userIndRightInfo.setSysModId(null);
        userIndRightInfo.setSysModDtm(null);
        return userIndRightInfo;
    }

    public Mono<ServerResponse> updateUserByQuery(ServerRequest request) {
        return userRepository.updateUserInfoBySql("icarus", "루시퍼", "icarus@gmail.com")
                .flatMap(unused -> ServerResponse.ok().build());
    }

    public Mono<ServerResponse> updateUserByTemplate(ServerRequest request) {
        User user = new User();
        user.setId("icarus");
        user.setEmailAddr("icaris@gmail.com");
        user.setName("루시퍼");
        return userRepository.updateByTemplate(user)
                .flatMap(ServerResponse.ok()::bodyValue);

    }

    public Mono<ServerResponse> updateUserByDatabaseClient(ServerRequest request) {
        User user = new User();
        user.setId("icarus");
        user.setEmailAddr("icaris@gmail.com");
        user.setName("이승호");
        return userRepository.updateByDatabaseClient(user)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findByQueryMethod(ServerRequest request) {
        LocalDateTime startDay = LocalDateTime.of(2021, 3, 1, 0, 0);
        LocalDateTime endDay = LocalDateTime.now();
        return userRepository.findUsersBySysModDtmBetween(startDay, endDay)
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> compositeKey(ServerRequest request) {
        return indRightInfoRepository.findById("kyungbok", "01")
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> userDetail(ServerRequest request) {
        return userRepository.findById("kyungbok")
                .zipWith(indRightInfoRepository.findByUserId("kyungbok").collectList(), UserDetail::new)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> userDetail2(ServerRequest request) {
        return userRepository.findById("kyungbok")
                .map(user -> {
                    UserDetail userDetail = new UserDetail();
                    userDetail.setUser(user);
                    return userDetail;
                }).flatMap(userDetail ->
                        indRightInfoRepository.findByUserId(userDetail.getUser().getId())
                            .collectList()
                            .map(userIndRightInfo -> {
                                userDetail.setUserIndRightInfo(userIndRightInfo);
                                return userDetail;
                            })
                ).flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> userDetailZipWhen(ServerRequest request) {
        return userRepository.findById("kyungbok")
                .zipWhen(user -> indRightInfoRepository.findByUserId(user.getId()).collectList())
                .map(tuple -> new UserDetail(tuple.getT1(), tuple.getT2()))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findAllUserDetail(ServerRequest request) {
        return userRepository.findAll()
                .map(Mono::just)
                .flatMap(userMono ->  userMono.zipWhen(user -> indRightInfoRepository.findByUserId(user.getId()).collectList())
                            .map(tuple -> new UserDetail(tuple.getT1(), tuple.getT2())))
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findAllUserDetailMultiPipe(ServerRequest request) {
        Flux<User> userFlux = userRepository.findAll().cache();
        Flux<UserDetail> userDetailFlux = userFlux.map(user -> {
            UserDetail userDetail = new UserDetail();
            userDetail.setUser(user);
            return userDetail;
        });
        Flux<List<UserIndRightInfo>> listFlux = userFlux.map(User::getId)
                .flatMap(id -> indRightInfoRepository.findByUserId(id).collectList());
        return Flux.zip(userDetailFlux, listFlux, UserDetail::withUserIndRightInfo)
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);

    }

    public Mono<ServerResponse> findAllUserDetailMultiPipe2(ServerRequest request) {
        Flux<User> allUserFlux = userRepository.findAll().cache();
        Flux<UserDetail> emptyUserDetailFlux = allUserFlux.map(user -> new UserDetail()).log();
        Flux<List<UserIndRightInfo>> indRightInfoListFlux = allUserFlux.map(User::getId)
                .flatMap(id -> indRightInfoRepository.findByUserId(id).collectList()).log();
        return emptyUserDetailFlux
                .zipWith(allUserFlux, UserDetail::withUser)
                .log()
                .zipWith(indRightInfoListFlux, UserDetail::withUserIndRightInfo)
                .log()
                .collectList()
                .log()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findAllUserDetailMultiPipeParallel(ServerRequest request) {
        Flux<User> userFlux = userRepository.findAll().cache().publishOn(Schedulers.parallel()).log();
        Flux<List<UserIndRightInfo>> indRightInfoListFlux = userFlux.map(User::getId)
                .flatMap(id -> indRightInfoRepository.findByUserId(id).log().collectList());
        return Flux.zip(userFlux, indRightInfoListFlux, UserDetail::new)
                .log()
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findAllUserEmbeddedList(ServerRequest request) {
        return userRepository.findAll()
                .flatMap(user -> Mono.just(user).zipWith(indRightInfoRepository.findByUserId(user.getId()).collectList(), User::withUserIndRightInfoList))
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findAllUserEmbeddedList2(ServerRequest request) {
        return userRepository.findAll()
                .map(Mono::just)
                .flatMap(userMono -> userMono.zipWhen(user -> indRightInfoRepository.findByUserId(user.getId()).collectList(), User::withUserIndRightInfoList))
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> findAllUserEmbeddedListMultiPipeParallel(ServerRequest request) {
        Flux<User> userFlux = userRepository.findAll()
                .cache()
                .publishOn(Schedulers.parallel())
                .log();
        Flux<List<UserIndRightInfo>> indRightInfoListFlux = userFlux.map(User::getId)
                .flatMap(id -> indRightInfoRepository.findByUserId(id)
                        .log()
                        .collectList());
        return Flux.zip(userFlux, indRightInfoListFlux, User::withUserIndRightInfoList)
                .log()
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> interfaceProjection(ServerRequest request) {
        return userRepository.findCellphoneById("kyungbok")
                .map(cellphone -> String.join("-", cellphone.getCellSctNo(), cellphone.getCellTxnoNo(), cellphone.getCellEndNo()))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> dtoProjection(ServerRequest request) {
        return userRepository.findTelephoneById("kyungbok")
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> dynamicProjection(ServerRequest request) {
        String phoneTypeVariable = request.pathVariable("phoneType");
        PhoneType type = PhoneType.valueOf(phoneTypeVariable.toUpperCase());
        Mono<?> result = null;
        switch (type) {
            case CELLPHONE: result = userRepository.findById("kyungbok", Cellphone.class);
                break;
            case TELEPHONE: result = userRepository.findById("kyungbok", Telephone.class);
                break;
        }
        return result.flatMap(ServerResponse.ok()::bodyValue);
    }

    enum PhoneType {
        CELLPHONE, TELEPHONE
    }

    public Mono<ServerResponse> paging(ServerRequest request) {
        //Page<T>객체를 R2dbc에선 지원하지 않는다.
        return userRepository.findAllBy(PageRequest.of(1, 3, Sort.by(Sort.Order.asc("id"))))
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> projectionTest(ServerRequest request) {
        return userRepository.findById("kyungbok", PersonalInfo.class)
                .log()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

}
