package com.example.back.model;

import lombok.Data;

@Data
public class Company {
    private Long id;
    private Long userId;
    private String name;
    private String catchPhrase;
    private String bs;
} 