package com.example.back.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;
    
    @Mock
    private UserDetailsImpl userDetails;
    
    @Mock
    private Authentication authentication;
    
    private String jwtToken;
    
    @BeforeEach
    public void setup() {
        // Set private fields using reflection
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKeyVerySecureAndLongEnoughForHmacSha256Algorithm");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 60000); // 1 minute
        
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        
        // Generate a valid token for tests
        jwtToken = jwtUtils.generateJwtToken(authentication);
    }
    
    @Test
    public void testGenerateJwtToken() {
        // Execute - token already generated in setup
        
        // Verify
        assertThat(jwtToken).isNotNull();
        assertThat(jwtToken).isNotEmpty();
    }
    
    @Test
    public void testGetUsernameFromJwtToken() {
        // Execute
        String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
        
        // Verify
        assertThat(username).isEqualTo("test@example.com");
    }
    
    @Test
    public void testValidateJwtToken_Valid() {
        // Execute & Verify
        assertTrue(jwtUtils.validateJwtToken(jwtToken));
    }
    
    @Test
    public void testValidateJwtToken_Invalid() {
        // Execute & Verify
        assertFalse(jwtUtils.validateJwtToken("invalid.jwt.token"));
    }
    
    @Test
    public void testValidateJwtToken_Expired() {
        // Setup: Create a JWT with very short expiration
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 1); // 1 millisecond
        String shortLivedToken = jwtUtils.generateJwtToken(authentication);
        
        // Wait a moment to ensure token expires
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Execute & Verify
        assertFalse(jwtUtils.validateJwtToken(shortLivedToken));
    }
} 