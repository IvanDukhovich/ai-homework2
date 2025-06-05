package com.example.back.controller;

import com.example.back.dto.request.UserRequest;
import com.example.back.dto.response.UserResponse;
import com.example.back.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse userResponse;
    private UserRequest userRequest;

    @BeforeEach
    public void setup() {
        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("Test User");
        userResponse.setUsername("testuser");
        userResponse.setEmail("test@example.com");
        userResponse.setPhone("123-456-7890");
        userResponse.setWebsite("example.com");

        userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setUsername("testuser");
        userRequest.setEmail("test@example.com");
        userRequest.setPhone("123-456-7890");
        userRequest.setWebsite("example.com");
    }

    @Test
    @WithMockUser
    public void testGetAllUsers() throws Exception {
        when(userService.findAll()).thenReturn(Arrays.asList(userResponse));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(userResponse.getId()))
                .andExpect(jsonPath("$[0].name").value(userResponse.getName()))
                .andExpect(jsonPath("$[0].email").value(userResponse.getEmail()));
    }

    @Test
    @WithMockUser
    public void testGetUserById() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.of(userResponse));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.name").value(userResponse.getName()))
                .andExpect(jsonPath("$.email").value(userResponse.getEmail()));
    }

    @Test
    @WithMockUser
    public void testGetUserByIdNotFound() throws Exception {
        when(userService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testCreateUser() throws Exception {
        when(userService.create(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.name").value(userResponse.getName()))
                .andExpect(jsonPath("$.email").value(userResponse.getEmail()));
    }

    @Test
    @WithMockUser
    public void testUpdateUser() throws Exception {
        when(userService.update(eq(1L), any(UserRequest.class))).thenReturn(Optional.of(userResponse));

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.name").value(userResponse.getName()))
                .andExpect(jsonPath("$.email").value(userResponse.getEmail()));
    }

    @Test
    @WithMockUser
    public void testDeleteUser() throws Exception {
        when(userService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }
} 