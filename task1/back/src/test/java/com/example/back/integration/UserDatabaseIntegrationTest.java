package com.example.back.integration;

import com.example.back.config.H2TestDbConfig;
import com.example.back.config.TestSecurityConfig;
import com.example.back.dto.request.UserRequest;
import com.example.back.dto.response.UserResponse;
import com.example.back.mapper.UserMapper;
import com.example.back.model.User;
import com.example.back.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test-db")
@Import({H2TestDbConfig.class, TestSecurityConfig.class})
public class UserDatabaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Clear previous test data
        List<User> existingUsers = userMapper.findAll();
        for (User user : existingUsers) {
            userMapper.delete(user.getId());
        }
        
        // Set up test user
        User testUser = new User();
        testUser.setName("Test Database User");
        testUser.setUsername("testdbuser");
        testUser.setEmail("testdb@example.com");
        testUser.setPhone("123-456-7890");
        testUser.setWebsite("testdb.com");
        
        // Setup request for creating users
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Test Database User");
        userRequest.setUsername("testdbuser");
        userRequest.setEmail("testdb@example.com");
        userRequest.setPhone("123-456-7890");
        userRequest.setWebsite("testdb.com");
        
        // Insert test user
        userMapper.insert(testUser);
    }
    
    @Test
    @WithMockUser
    public void testUserCrudOperationsWithDatabase() throws Exception {
        // Verify user exists in database
        Optional<User> storedUser = userMapper.findByEmail("testdb@example.com");
        assertThat(storedUser).isPresent();
        Long userId = storedUser.get().getId();
        
        // Test GET all users
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("testdb@example.com"));
        
        // Test GET user by ID
        mockMvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testdb@example.com"));
        
        // Test CREATE new user
        UserRequest newUser = new UserRequest();
        newUser.setName("New DB User");
        newUser.setUsername("newdbuser");
        newUser.setEmail("newdb@example.com");
        newUser.setPhone("555-555-5555");
        newUser.setWebsite("newdb.com");
        
        String newUserJson = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        UserResponse createdUser = objectMapper.readValue(newUserJson, UserResponse.class);
        
        // Verify new user exists in database
        Optional<User> newStoredUser = userMapper.findByEmail("newdb@example.com");
        assertThat(newStoredUser).isPresent();
        
        // Test UPDATE user
        newUser.setName("Updated DB User");
        
        mockMvc.perform(put("/api/users/" + createdUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated DB User"));
        
        // Verify user was updated in database
        Optional<User> updatedUser = userMapper.findById(createdUser.getId());
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getName()).isEqualTo("Updated DB User");
        
        // Test DELETE user
        mockMvc.perform(delete("/api/users/" + createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
        
        // Verify user was deleted from database
        Optional<User> deletedUser = userMapper.findById(createdUser.getId());
        assertThat(deletedUser).isEmpty();
    }
} 