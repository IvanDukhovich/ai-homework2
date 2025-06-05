package com.example.back.service;

import com.example.back.dto.request.UserRequest;
import com.example.back.dto.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponse> findAll();
    Optional<UserResponse> findById(Long id);
    UserResponse create(UserRequest userRequest);
    Optional<UserResponse> update(Long id, UserRequest userRequest);
    boolean delete(Long id);
} 