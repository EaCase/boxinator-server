package com.example.boxinator.dtos.shipment;

import com.example.boxinator.dtos.box.BoxTierGetDto;
import com.example.boxinator.dtos.country.CountryGetDto;

import lombok.Data;

import java.util.List;

@Data
public class ShipmentGetDto {
    private Long id;
    private Long accountId;
    private BoxTierGetDto boxTier;
    private String boxColor;
    private String recipient;
    private CountryGetDto country;
    private Float cost;
    private List<ShipmentStatusGetDto> statuses;
}
