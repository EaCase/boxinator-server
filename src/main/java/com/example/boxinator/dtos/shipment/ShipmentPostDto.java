package com.example.boxinator.dtos.shipment;

import lombok.Data;

@Data
public class ShipmentPostDto {
    private String recipient;
    private String boxColor;
    private Long boxTierId;
    private Long countryId;
}
