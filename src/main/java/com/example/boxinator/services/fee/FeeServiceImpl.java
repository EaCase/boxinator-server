package com.example.boxinator.services.fee;

import com.example.boxinator.models.fee.Fee;
import com.example.boxinator.repositories.fee.FeeRepository;
import org.springframework.stereotype.Service;

@Service
public class FeeServiceImpl implements FeeService {
    private final FeeRepository feeRepository;

    public FeeServiceImpl(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    @Override
    public Fee getBaseShipmentFee() {
        return feeRepository.getBaseShippingFee();
    }
}
