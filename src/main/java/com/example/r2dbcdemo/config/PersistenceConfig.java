package com.example.r2dbcdemo.config;

import com.example.r2dbcdemo.config.auditor.SampleAuditorAware;
import com.example.r2dbcdemo.config.converter.OrgRoleReadConverter;
import com.example.r2dbcdemo.config.converter.OrgRoleWriterConverter;
import com.example.r2dbcdemo.domain.OrgRole;
import com.example.r2dbcdemo.repository.UserRepository;
import com.example.r2dbcdemo.repository.entity.User;
import com.example.r2dbcdemo.util.Util;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.DatabasePopulator;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableTransactionManagement
@EnableR2dbcAuditing
public class PersistenceConfig extends AbstractR2dbcConfiguration{

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(localResourcePopulator());
        return initializer;
    }

    private DatabasePopulator localResourcePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScripts(new ClassPathResource("/schema.sql"));
        populator.addScripts(new ClassPathResource("/data.sql"));
        return populator;
    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get("r2dbc:h2:mem");
    }

    @Override
    protected List<Object> getCustomConverters() {
        List<Object> converterList = new ArrayList<>();
        converterList.add(new OrgRoleReadConverter());
        converterList.add(new OrgRoleWriterConverter());
//        Converter<OrgRole, String> orgRoleToString = OrgRole::getCode; // 작동안함
//        converterList.add(orgRoleToString);
        return converterList;
    }

    @Bean
    public ReactiveAuditorAware<String> userAuditorProvider() {
        return new SampleAuditorAware();
    }
}
