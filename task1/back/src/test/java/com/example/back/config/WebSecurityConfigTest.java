package com.example.back.config;

import com.example.back.security.AuthTokenFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class WebSecurityConfigTest {

    @InjectMocks
    private WebSecurityConfig securityConfig;

    @Test
    public void testPasswordEncoderBean() {
        // Execute
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        // Verify
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);

        // Test encoding
        String encoded = passwordEncoder.encode("password");
        assertThat(encoded).isNotEqualTo("password");
        assertThat(passwordEncoder.matches("password", encoded)).isTrue();
    }

    @Test
    public void testAuthTokenFilterBean() {
        // Execute
        AuthTokenFilter filter = securityConfig.authenticationJwtTokenFilter();

        // Verify
        assertThat(filter).isNotNull();
        assertThat(filter).isInstanceOf(AuthTokenFilter.class);
    }

}