package com.example.back.controller;

import com.example.back.config.TestConfig;
import com.example.back.dto.request.LoginRequest;
import com.example.back.dto.request.SignupRequest;
import com.example.back.mapper.AuthMapper;
import com.example.back.model.UserAuth;
import com.example.back.security.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setup() {
        signupRequest = new SignupRequest();
        signupRequest.setName("Integration Test User");
        signupRequest.setEmail("integration@example.com");
        signupRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("integration@example.com");
        loginRequest.setPassword("password123");
        
        // Mock behavior
        Authentication mockAuth = any(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuth);
        when(jwtUtils.generateJwtToken(any(Authentication.class)))
            .thenReturn("test-jwt-token");
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("integration@example.com");
    }

    @Test
    public void testSignupAndSignin() throws Exception {
        // Setup for signup - user doesn't exist yet
        UserAuth userAuth = new UserAuth();
        userAuth.setId(1L);
        userAuth.setEmail(signupRequest.getEmail());
        
        when(authMapper.findByEmail(signupRequest.getEmail()))
            .thenReturn(Optional.empty()) // First call during signup
            .thenReturn(Optional.of(userAuth)); // Subsequent calls
        
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

        // Step 1: Register a new user
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // Step 2: Login with the registered user
        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value(loginRequest.getEmail()))
                .andReturn();
        
        // Extract token from response
        String responseContent = result.getResponse().getContentAsString();
        String authToken = "Bearer " + objectMapper.readTree(responseContent).get("token").asText();
    }

    @Test
    public void testAccessProtectedResourceWithAndWithoutToken() throws Exception {
        // Setup
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);
        
        // Step 1: Try to access protected resource without token
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());

        // Step 2: Access with valid token
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer test-jwt-token"))
                .andExpect(status().isOk());

        // Step 3: Access with invalid token
        when(jwtUtils.validateJwtToken("invalid-token")).thenReturn(false);
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }
} 