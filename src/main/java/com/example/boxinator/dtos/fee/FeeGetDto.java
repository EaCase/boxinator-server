package com.example.boxinator.dtos.fee;

import lombok.Data;

/**
 * Common DTO for fees (e.g. Shipping total, Base fees).
 */
@Data
public class FeeGetDto {
    private String name;
    private String amount;
}
