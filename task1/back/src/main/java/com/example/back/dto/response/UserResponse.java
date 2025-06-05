package com.example.back.dto.response;

import com.example.back.model.Address;
import com.example.back.model.Company;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;
} 