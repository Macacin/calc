package com.example.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ManufacturerDto {
    private Long id;
    private String name;
    private String country;
}