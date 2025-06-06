package com.example.back.mapper;

import com.example.back.model.UserAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthMapperTest {
    
    @Mock
    private AuthMapper authMapper;
    
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
    public void testFindByEmail() {
        // Setup
        when(authMapper.findByEmail("test@example.com")).thenReturn(Optional.of(testUserAuth));
        when(authMapper.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        
        // Execute & Verify: Existing user
        Optional<UserAuth> foundUser = authMapper.findByEmail("test@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Test User");
        
        // Execute & Verify: Non-existing user
        Optional<UserAuth> notFoundUser = authMapper.findByEmail("nonexistent@example.com");
        assertThat(notFoundUser).isEmpty();
    }
    
    @Test
    public void testInsert() {
        // Setup
        when(authMapper.insert(any(UserAuth.class))).thenAnswer(invocation -> {
            UserAuth userAuth = invocation.getArgument(0);
            userAuth.setId(2L);
            return 1;
        });
        
        UserAuth newUserAuth = new UserAuth();
        newUserAuth.setName("New User");
        newUserAuth.setEmail("new@example.com");
        newUserAuth.setPasswordHash("hashedPassword");
        
        // Execute
        int result = authMapper.insert(newUserAuth);
        
        // Verify
        assertThat(result).isEqualTo(1);
        assertThat(newUserAuth.getId()).isEqualTo(2L);
    }
} 