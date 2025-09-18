package com.backend.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * Bean for ModelMapper
     *
     * @return ModelMapper instance
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}