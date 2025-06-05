package com.example.back.model;

import lombok.Data;

@Data
public class Geo {
    private Long id;
    private Long addressId;
    private String lat;
    private String lng;
} 