package com.example.boxinator.services.fee;

import com.example.boxinator.models.fee.Fee;
import org.springframework.stereotype.Service;

@Service
public interface FeeService {
    Fee getBaseShipmentFee();
    Fee calculateShipmentCost(Long countryId, Long boxTierId);
}
