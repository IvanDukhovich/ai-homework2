package com.example.back.dto.request;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class AddressRequest {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    
    @Valid
    private GeoRequest geo;
} 