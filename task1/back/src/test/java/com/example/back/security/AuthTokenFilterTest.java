package com.example.back.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;
    
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    
    @InjectMocks
    private AuthTokenFilter authTokenFilter;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;
    
    @Mock
    private UserDetails userDetails;
    
    @BeforeEach
    public void setup() {
        SecurityContextHolder.clearContext();
    }
    
    @Test
    public void testDoFilterInternal_WithValidJwt() throws ServletException, IOException {
        // Setup
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(jwtUtils.validateJwtToken("valid-token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid-token")).thenReturn("test@example.com");
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        
        // Execute
        authTokenFilter.doFilterInternal(request, response, filterChain);
        
        // Verify
        verify(filterChain).doFilter(request, response);
        verify(userDetailsService).loadUserByUsername("test@example.com");
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    }
    
    @Test
    public void testDoFilterInternal_WithNoJwt() throws ServletException, IOException {
        // Setup
        when(request.getHeader("Authorization")).thenReturn(null);
        
        // Execute
        authTokenFilter.doFilterInternal(request, response, filterChain);
        
        // Verify
        verify(filterChain).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
    
    @Test
    public void testDoFilterInternal_WithInvalidJwt() throws ServletException, IOException {
        // Setup
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
        when(jwtUtils.validateJwtToken("invalid-token")).thenReturn(false);
        
        // Execute
        authTokenFilter.doFilterInternal(request, response, filterChain);
        
        // Verify
        verify(filterChain).doFilter(request, response);
        verify(jwtUtils).validateJwtToken("invalid-token");
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
    
    @Test
    public void testDoFilterInternal_WithInvalidAuthHeaderFormat() throws ServletException, IOException {
        // Setup - Auth header without Bearer prefix
        when(request.getHeader("Authorization")).thenReturn("invalid-format-token");
        
        // Execute
        authTokenFilter.doFilterInternal(request, response, filterChain);
        
        // Verify
        verify(filterChain).doFilter(request, response);
        verify(jwtUtils, never()).validateJwtToken(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }
} 