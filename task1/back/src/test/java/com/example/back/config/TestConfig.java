package com.example.back.config;

import com.example.back.mapper.*;
import com.example.back.security.JwtUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Profile("test")
public class TestConfig {

    @Bean
    @Primary
    public UserMapper userMapper() {
        return mock(UserMapper.class);
    }
    
    @Bean
    @Primary
    public AddressMapper addressMapper() {
        return mock(AddressMapper.class);
    }
    
    @Bean
    @Primary
    public CompanyMapper companyMapper() {
        return mock(CompanyMapper.class);
    }
    
    @Bean
    @Primary
    public GeoMapper geoMapper() {
        return mock(GeoMapper.class);
    }
    
    @Bean
    @Primary
    public AuthMapper authMapper() {
        return mock(AuthMapper.class);
    }
    
    @Bean
    @Primary
    public JwtUtils jwtUtils() {
        return mock(JwtUtils.class);
    }
    
    @Bean
    @Primary
    public AuthenticationManager authenticationManager() {
        return mock(AuthenticationManager.class);
    }
    
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 