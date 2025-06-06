package com.example.back.controller;

import com.example.back.config.TestConfig;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    private UserResponse testUserResponse;

    @BeforeEach
    public void setup() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("123-456-7890");
        testUser.setWebsite("test.com");

        testUserResponse = new UserResponse();
        testUserResponse.setId(1L);
        testUserResponse.setName("Test User");
        testUserResponse.setUsername("testuser");
        testUserResponse.setEmail("test@example.com");
        testUserResponse.setPhone("123-456-7890");
        testUserResponse.setWebsite("test.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("John Doe");
        user2.setUsername("johndoe");
        user2.setEmail("john@example.com");

        UserResponse user2Response = new UserResponse();
        user2Response.setId(2L);
        user2Response.setName("John Doe");
        user2Response.setUsername("johndoe");
        user2Response.setEmail("john@example.com");

        // Setup mock responses
        when(userMapper.findAll()).thenReturn(Arrays.asList(testUser, user2));
        when(userMapper.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.findById(999L)).thenReturn(Optional.empty());
    }

    @Test
    @WithMockUser
    public void testGetAllUsers() throws Exception {
        when(userService.findAll()).thenReturn(Collections.singletonList(testUserResponse));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[0].email", notNullValue()));
    }

    @Test
    @WithMockUser
    public void testGetUserById() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.of(testUserResponse));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test User")))
                .andExpect(jsonPath("$.email", is("test@example.com")));
    }

    @Test
    @WithMockUser
    public void testGetUserByIdNotFound() throws Exception {
        when(userService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testCreateAndUpdateAndDeleteUser() throws Exception {
        // Setup for create
        UserRequest userRequest = new UserRequest();
        userRequest.setName("New User");
        userRequest.setUsername("newuser");
        userRequest.setEmail("new@example.com");
        userRequest.setPhone("555-123-4567");
        userRequest.setWebsite("newuser.com");

        UserResponse newUserResponse = new UserResponse();
        newUserResponse.setId(3L);
        newUserResponse.setName("New User");
        newUserResponse.setUsername("newuser");
        newUserResponse.setEmail("new@example.com");

        when(userService.create(any(UserRequest.class))).thenReturn(newUserResponse);

        // Test create
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("New User")))
                .andExpect(jsonPath("$.email", is("new@example.com")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Setup for update
        UserResponse updatedUserResponse = new UserResponse();
        updatedUserResponse.setId(3L);
        updatedUserResponse.setName("Updated User");
        updatedUserResponse.setUsername("newuser");
        updatedUserResponse.setEmail("new@example.com");

        when(userService.update(anyLong(), any(UserRequest.class))).thenReturn(Optional.of(updatedUserResponse));

        // Test update
        userRequest.setName("Updated User");
        mockMvc.perform(put("/api/users/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Updated User")))
                .andExpect(jsonPath("$.email", is("new@example.com")));

        // Setup for delete
        when(userService.delete(3L)).thenReturn(true);
        when(userService.findById(3L)).thenReturn(Optional.empty());

        // Test delete
        mockMvc.perform(delete("/api/users/3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User deleted successfully")));

        // Verify user is deleted
        mockMvc.perform(get("/api/users/3"))
                .andExpect(status().isNotFound());
    }

}