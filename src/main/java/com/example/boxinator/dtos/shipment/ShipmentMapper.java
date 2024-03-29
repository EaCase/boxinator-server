package com.example.boxinator.dtos.shipment;

import com.example.boxinator.dtos.box.BoxTierGetDto;
import com.example.boxinator.dtos.box.BoxTierMapper;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.box.BoxTier;
import com.example.boxinator.models.country.Country;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.repositories.box.BoxRepository;
import com.example.boxinator.repositories.country.CountryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring")
public abstract class ShipmentMapper {

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private BoxTierMapper boxMapper;

    @Mapping(source = "boxTier", target = "boxTier", qualifiedByName = "boxToDto")
    @Mapping(source = "account", target = "accountId", qualifiedByName = "accountToId")
    public abstract ShipmentGetDto toShipmentDto(Shipment shipment);

    @Mapping(source = "boxTierId", target = "boxTier", qualifiedByName = "boxTierIdToBoxTier")
    @Mapping(source = "countryId", target = "country", qualifiedByName = "countryIdToCountry")
    public abstract Shipment toShipment(ShipmentPostDto dto);

    @Named("boxToDto")
    public BoxTierGetDto boxToDto(BoxTier boxTier) {
        return boxMapper.toDto(boxTier);
    }

    @Named("boxTierIdToBoxTier")
    public BoxTier boxTierIdToBoxTier(Long id) {
        return boxRepository.findById(id).orElseThrow(() -> new ApplicationException(
                "Could not find a box tier matching the provided id.",
                HttpStatus.NOT_FOUND
        ));
    }

    @Named("countryIdToCountry")
    public Country countryIdToCountry(Long id) {
        return countryRepository.findById(id).orElseThrow(() -> new ApplicationException(
                "Could not find a country matching the provided id.",
                HttpStatus.NOT_FOUND
        ));
    }

    @Named("accountToId")
    public Long accountToId(Account acc) {
        return acc.getId();
    }
}
