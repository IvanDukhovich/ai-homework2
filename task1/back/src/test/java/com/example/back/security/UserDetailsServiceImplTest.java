package com.example.back.security;

import com.example.back.mapper.AuthMapper;
import com.example.back.model.UserAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {
    
    @Mock
    private AuthMapper authMapper;
    
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    
    private UserAuth testUserAuth;
    
    @BeforeEach
    public void setup() {
        testUserAuth = new UserAuth();
        testUserAuth.setId(1L);
        testUserAuth.setName("Test User");
        testUserAuth.setEmail("test@example.com");
        testUserAuth.setPasswordHash("hashedPassword");
    }
    
    @Test
    public void testLoadUserByUsername_Success() {
        // Setup
        when(authMapper.findByEmail("test@example.com")).thenReturn(Optional.of(testUserAuth));
        
        // Execute
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");
        
        // Verify
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("hashedPassword");
    }
    
    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Setup
        when(authMapper.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        
        // Execute & Verify
        assertThrows(UsernameNotFoundException.class, () -> 
            userDetailsService.loadUserByUsername("nonexistent@example.com"));
    }
    
    @Test
    public void testUserDetailsImpl_Authorities() {
        // Setup
        when(authMapper.findByEmail("test@example.com")).thenReturn(Optional.of(testUserAuth));
        
        // Execute
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("test@example.com");
        
        // Verify - expecting empty or default authorities, not null
        assertThat(userDetails.getAuthorities()).isNotNull();
    }
    
    @Test
    public void testUserDetailsImpl_AccountNonExpired() {
        // Setup
        when(authMapper.findByEmail("test@example.com")).thenReturn(Optional.of(testUserAuth));
        
        // Execute
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("test@example.com");
        
        // Verify
        assertThat(userDetails.isAccountNonExpired()).isTrue();
    }
    
    @Test
    public void testUserDetailsImpl_AccountNonLocked() {
        // Setup
        when(authMapper.findByEmail("test@example.com")).thenReturn(Optional.of(testUserAuth));
        
        // Execute
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("test@example.com");
        
        // Verify
        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }
} 