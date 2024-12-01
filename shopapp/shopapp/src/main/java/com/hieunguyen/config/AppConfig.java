package com.hieunguyen.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // Định nghĩa một Bean ModelMapper
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();  // Trả về một instance của ModelMapper
    }
}
