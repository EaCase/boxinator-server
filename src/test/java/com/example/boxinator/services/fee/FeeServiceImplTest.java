package com.example.boxinator.services.fee;

import com.example.boxinator.models.box.BoxTier;
import com.example.boxinator.models.country.Country;
import com.example.boxinator.models.country.CountryTier;
import com.example.boxinator.models.fee.Fee;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.repositories.box.BoxRepository;
import com.example.boxinator.repositories.country.CountryRepository;
import com.example.boxinator.repositories.fee.FeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import javax.swing.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

class FeeServiceImplTest {
    private FeeServiceImpl service;
    private FeeRepository feeRepository;
    private CountryRepository countryRepository;
    private BoxRepository boxRepository;

    @BeforeEach
    void setUp() {
        feeRepository = Mockito.mock(FeeRepository.class);
        countryRepository = Mockito.mock(CountryRepository.class);
        boxRepository = Mockito.mock(BoxRepository.class);

        service = new FeeServiceImpl(
            feeRepository,
            countryRepository,
            boxRepository
        );
    }

    // Testing for getting the base shipment fee
    @Test
    void getBaseShipmentFee() {

        Fee mockFee = Mockito.mock(Fee.class);
        Mockito.when(mockFee.getAmount()).thenReturn(200F);

        Mockito.when(feeRepository.getBaseShippingFee()).thenReturn(mockFee);

        var fee = service.getBaseShipmentFee();

        assertEquals(200, fee.getAmount());

        System.out.println("Expected 200 " + "Base shipment fee: " + fee.getAmount());

    }

    // Testing calculating complete shipment cost
    @Test
    void calculateShipmentCost() {
        //  https://devwithus.com/java-spring-mock-repository/
        BoxTier boxTier = Mockito.mock(BoxTier.class);
        Mockito.when(boxTier.getWeight()).thenReturn(2F);
        Mockito.when(boxTier.getName()).thenReturn("Humble");

        CountryTier tier = Mockito.mock(CountryTier.class);
        Mockito.when(tier.getShippingMultiplier()).thenReturn(200F);

        Country country = Mockito.mock(Country.class);
        Mockito.when(country.getTier()).thenReturn(tier);
        Mockito.when(country.getName()).thenReturn("Denmark");

        Fee mockFee = Mockito.mock(Fee.class);
        Mockito.when(mockFee.getAmount()).thenReturn(200F);

        Mockito.when(feeRepository.getBaseShippingFee()).thenReturn(mockFee);
        Mockito.when(countryRepository.findById(anyLong())).thenReturn(Optional.of(country));
        Mockito.when(boxRepository.findById(anyLong())).thenReturn(Optional.of(boxTier));

        Fee fee = service.calculateShipmentCost(0L,0L);
        assertEquals(600F, fee.getAmount());
        assertEquals("Shipment cost for Humble to Denmark", fee.getName());
    }
}