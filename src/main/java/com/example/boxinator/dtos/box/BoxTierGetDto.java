package com.example.boxinator.dtos.box;

import lombok.Data;

@Data
public class BoxTierGetDto {
    private Long id;
    private String name;
    private Float weight;
}
