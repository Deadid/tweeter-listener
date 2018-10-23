package org.tweetter.listener.config;

import com.coxautodev.graphql.tools.SchemaParser;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import graphql.servlet.ObjectMapperConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tweetter.listener.resolvers.Subscription;


@Configuration
public class GraphQLConfig {


    @Bean
    public ObjectMapperConfigurer objectMapperConfigurer() {
        return (mapper -> mapper.registerModule(new JavaTimeModule()));
    }
}
