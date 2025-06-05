package com.example.back.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserAuth {
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;
} 