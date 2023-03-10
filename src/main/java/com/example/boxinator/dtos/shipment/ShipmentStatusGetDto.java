package com.example.boxinator.dtos.shipment;

import com.example.boxinator.models.shipment.Status;
import lombok.Data;

@Data
public class ShipmentStatusGetDto {
    private Status status;
    private String timestamp;
}
