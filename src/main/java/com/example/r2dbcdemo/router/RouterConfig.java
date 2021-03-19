package com.example.r2dbcdemo.router;

import com.example.r2dbcdemo.handler.SampleHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/find-all", beanClass = SampleHandler.class, beanMethod = "findAll"
                    , operation = @Operation(operationId = "findAll", description = "User 리스트 조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/create-sample", beanClass = SampleHandler.class, beanMethod = "createUser"
                    , operation = @Operation(operationId = "createUser", description = "Sample User 생성, 2번하면 PK 오류발생"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/update-by-query", beanClass = SampleHandler.class, beanMethod = "updateUserByQuery"
                    , operation = @Operation(operationId = "updateUserByQuery", description = "@Query로 업데이트"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/update-by-template", beanClass = SampleHandler.class, beanMethod = "updateUserByTemplate"
                    , operation = @Operation(operationId = "updateUserByTemplate", description = "R2dbcEntityTemplate으로 업데이트"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/update-by-dbclient", beanClass = SampleHandler.class, beanMethod = "updateUserByDatabaseClient"
                    , operation = @Operation(operationId = "updateUserByDatabaseClient", description = "DatabaseClient로 업데이트"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/find-by-method", beanClass = SampleHandler.class, beanMethod = "findByQueryMethod"
                    , operation = @Operation(operationId = "findByQueryMethod", description = "Repository 메서드Query로 조회. modTime이 3월부터 현재까지인 유저만 조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/composite-key", beanClass = SampleHandler.class, beanMethod = "compositeKey"
                    , operation = @Operation(operationId = "compositeKey", description = "복합키 CustomRepository로 임시 구현"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/user-detail", beanClass = SampleHandler.class, beanMethod = "userDetail"
                    , operation = @Operation(operationId = "UserDetail", description = "UserDetail조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/interface-projection", beanClass = SampleHandler.class, beanMethod = "interfaceProjection"
                    , operation = @Operation(operationId = "interfaceProjection", description = "Interface projection으로 휴대폰번호만 추출 조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/dto-projection", beanClass = SampleHandler.class, beanMethod = "dtoProjection"
                    , operation = @Operation(operationId = "updateUserByTemplate", description = "Dto projection으로 전화번호 조회(Dto에 검색 조건 {id}가 없으면 동작 X)"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/dynamic-projection/{phoneType}", beanClass = SampleHandler.class, beanMethod = "dynamicProjection"
                    , operation = @Operation(operationId = "dynamicProjection"
                        , parameters = @Parameter(in = ParameterIn.PATH, name = "phoneType", description = "Dynamic Projection. PathVariable을 통해 동적 projection. (CELLPHONE, TELEPHONE)", example = "CELLPHONE"))),
            @RouterOperation(path = "/paging", beanClass = SampleHandler.class, beanMethod = "paging"
                    , operation = @Operation(operationId = "paging", description = "페이징처리 샘플"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/user-detail2", beanClass = SampleHandler.class, beanMethod = "userDetail2"
                    , operation = @Operation(operationId = "userDetail2", description = "UserDetail 조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/user-detail-zip-when", beanClass = SampleHandler.class, beanMethod = "userDetailZipWhen"
                    , operation = @Operation(operationId = "interfaceProjection", description = "zipWhen으로 UserDetail조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/find-all-user-detail", beanClass = SampleHandler.class, beanMethod = "findAllUserDetail"
                    , operation = @Operation(operationId = "findAllUserDetail", description = "전체 UserDetail조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/find-all-user-multi-pipe", beanClass = SampleHandler.class, beanMethod = "findAllUserDetailMultiPipe"
                    , operation = @Operation(operationId = "findAllUserDetailMultiPipe", description = "multi pipe로 전체 UserDetail 조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/find-all-user-multi-pipe2", beanClass = SampleHandler.class, beanMethod = "findAllUserDetailMultiPipe2"
                    , operation = @Operation(operationId = "findAllUserDetailMultiPipe2", description = "multi pipe로 전체 UserDetail 조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/find-all-user-multi-pipe-parallel", beanClass = SampleHandler.class, beanMethod = "findAllUserDetailMultiPipeParallel"
                    , operation = @Operation(operationId = "findAllUserDetailMultiPipeParallel", description = "multi pipe 병렬로 UserDetail 조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/find-all-user-embedded-list", beanClass = SampleHandler.class, beanMethod = "findAllUserEmbeddedList"
                    , operation = @Operation(operationId = "findAllUserEmbeddedList", description = "List를 포함하는 User 전체 조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/find-all-user-embedded-list2", beanClass = SampleHandler.class, beanMethod = "findAllUserEmbeddedList2"
                    , operation = @Operation(operationId = "findAllUserEmbeddedList2", description = "List를 포함하는 User 전체 조회2"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/find-all-user-embedded-list-multi-pipe-parallel", beanClass = SampleHandler.class, beanMethod = "findAllUserEmbeddedListMultiPipeParallel"
                    , operation = @Operation(operationId = "findAllUserEmbeddedListMultiPipeParallel", description = "List를 포함하는 User 병렬로 전체 조회"
                    , responses = {@ApiResponse(responseCode = "200", description = "Successful operation.")})),
            @RouterOperation(path = "/projection-test", beanClass = SampleHandler.class, beanMethod = "projectionTest")
    })
    public RouterFunction<ServerResponse> userRouter(SampleHandler sampleHandler) {
        return RouterFunctions.route()
                .nest(RequestPredicates.all(), builder -> {
                    builder.GET("/find-all", sampleHandler::findAll)
                        .GET("/create-sample", sampleHandler::createUser)
                        .GET("/update-by-query", sampleHandler::updateUserByQuery)
                        .GET("/update-by-template",sampleHandler::updateUserByTemplate)
                        .GET("/update-by-dbclient", sampleHandler::updateUserByDatabaseClient)
                        .GET("/find-by-method", sampleHandler::findByQueryMethod)
                        .GET("/composite-key", sampleHandler::compositeKey)
                        .GET("/user-detail", sampleHandler::userDetail)
                        .GET("/interface-projection", sampleHandler::interfaceProjection)
                        .GET("/dto-projection", sampleHandler::dtoProjection)
                        .GET("/dynamic-projection/{phoneType}", sampleHandler::dynamicProjection)
                        .GET("/paging", sampleHandler::paging)
                        .GET("/user-detail2", sampleHandler::userDetail2)
                        .GET("/user-detail-zip-when", sampleHandler::userDetailZipWhen)
                        .GET("/find-all-user-detail", sampleHandler::findAllUserDetail)
                        .GET("/find-all-user-multi-pipe",sampleHandler::findAllUserDetailMultiPipe)
                        .GET("/find-all-user-multi-pipe2", sampleHandler::findAllUserDetailMultiPipe2)
                        .GET("/find-all-user-multi-pipe-parallel", sampleHandler::findAllUserDetailMultiPipeParallel)
                        .GET("/find-all-user-embedded-list", sampleHandler::findAllUserEmbeddedList)
                        .GET("/find-all-user-embedded-list2", sampleHandler::findAllUserEmbeddedList2)
                        .GET("/find-all-user-embedded-list-multi-pipe-parallel", sampleHandler::findAllUserEmbeddedListMultiPipeParallel)
                        .GET("/projection-test", sampleHandler::projectionTest);
                })
                .build();
    }
}
