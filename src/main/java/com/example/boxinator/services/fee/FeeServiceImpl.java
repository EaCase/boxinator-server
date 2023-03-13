package com.example.boxinator.services.fee;

import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.box.BoxTier;
import com.example.boxinator.models.country.Country;
import com.example.boxinator.models.fee.Fee;
import com.example.boxinator.repositories.box.BoxRepository;
import com.example.boxinator.repositories.country.CountryRepository;
import com.example.boxinator.repositories.fee.FeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FeeServiceImpl implements FeeService {
    private final FeeRepository feeRepository;
    private final CountryRepository countryRepository;
    private final BoxRepository boxRepository;

    public FeeServiceImpl(
            FeeRepository feeRepository,
            CountryRepository countryRepository,
            BoxRepository boxRepository
        ) {
        this.feeRepository = feeRepository;
        this.countryRepository = countryRepository;
        this.boxRepository = boxRepository;
    }

    @Override
    public Fee getBaseShipmentFee() {
        return feeRepository.getBaseShippingFee();
    }

    @Override
    public Fee calculateShipmentCost(Long countryId, Long boxTierId) {
        Country country = countryRepository.findById(countryId).orElseThrow(() -> new ApplicationException(
                "Country with the id: "+ countryId + " could not be found",
                HttpStatus.NOT_FOUND
        ));
        BoxTier boxTier = boxRepository.findById(boxTierId).orElseThrow(() -> new ApplicationException(
                "Box tier with the id: "+ boxTierId + " could not be found",
                HttpStatus.NOT_FOUND
        ));

        Fee fee = new Fee();
        fee.setAmount(getBaseShipmentFee().getAmount() + boxTier.getWeight() * country.getTier().getShippingMultiplier());
        fee.setName("Shipment cost for " + boxTier.getName() + " to " + country.getName());
        return fee;
    }
}
