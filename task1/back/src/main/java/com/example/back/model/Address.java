package com.example.back.model;

import lombok.Data;

@Data
public class Address {
    private Long id;
    private Long userId;
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Geo geo;
} 