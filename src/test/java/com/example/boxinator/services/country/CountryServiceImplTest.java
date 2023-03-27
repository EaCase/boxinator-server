package com.example.boxinator.services.country;

import com.example.boxinator.dtos.country.CountryMapper;
import com.example.boxinator.dtos.country.CountryPostDto;
import com.example.boxinator.models.country.Country;
import com.example.boxinator.models.country.CountryTier;
import com.example.boxinator.repositories.country.CountryRepository;
import com.example.boxinator.repositories.country.CountryTierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


class CountryServiceImplTest {

    private CountryServiceImpl service;

    private CountryRepository countryRepository;
    private CountryTierRepository countryTierRepository;
    private CountryMapper countryMapper;


    @BeforeEach
    void setUp() {
        countryRepository = Mockito.mock(CountryRepository.class);
        countryTierRepository = Mockito.mock(CountryTierRepository.class);
        countryMapper = Mockito.mock(CountryMapper.class);

        service = new CountryServiceImpl(
                countryRepository,
                countryTierRepository,
                countryMapper
        );
    }
    /**
     * Used to create a shipment that is used in multiple tests
     * Used for DRY purposes
     **/
    private Country buildCountry(){

        CountryTier countryTier = Mockito.mock(CountryTier.class);
        Mockito.when(countryTier.getId()).thenReturn(1L);
        Mockito.when(countryTier.getName()).thenReturn("Tier 1");
        Mockito.when(countryTier.getShippingMultiplier()).thenReturn(1.0F);
        Mockito.when(countryTierRepository.getNonSourceTiers()).thenReturn(List.of(countryTier));

        Country country = Mockito.mock(Country.class);
        Mockito.when(country.getName()).thenReturn("Test Country");
        Mockito.when(country.getId()).thenReturn(1L);
        Mockito.when(countryMapper.toCountry(any())).thenReturn(country);
        Mockito.when(countryRepository.save(any())).thenReturn(country);

        return country;
    }

    /**
     * Testing creating a country
     **/
    @Test
    void createCountryTest() {

        CountryPostDto countryPostDto = new CountryPostDto();
        countryPostDto.setName("Test Country");


        CountryTier countryTier = Mockito.mock(CountryTier.class);
        Mockito.when(countryTier.getId()).thenReturn(1L);
        Mockito.when(countryTier.getName()).thenReturn("Tier 1");
        Mockito.when(countryTier.getShippingMultiplier()).thenReturn(1.0F);
        Mockito.when(countryTierRepository.getNonSourceTiers()).thenReturn(List.of(countryTier));

        Country country = Mockito.mock(Country.class);
        Mockito.when(country.getName()).thenReturn("Test Country");
        Mockito.when(country.getId()).thenReturn(1L);
        Mockito.when(countryMapper.toCountry(any())).thenReturn(country);
        Mockito.when(countryRepository.save(any())).thenReturn(country);


        Country createdCountry = service.create(countryPostDto);
        assertNotNull(createdCountry);
        assertEquals(country.getName(), createdCountry.getName());



    }

    /**
     * Test to get all shipments
     **/
    @Test
    void getAllCountriesTest() {

        List<Country> expectedCountries =  new ArrayList<>();
        expectedCountries.add(buildCountry());

        Mockito.when(countryRepository.findAll()).thenReturn(expectedCountries);

        List<Country> actualCountries = service.getAll();

         assertEquals(expectedCountries, actualCountries);



    }
    /**
     * Test to deleted a country by it's id
     **/
    @Test
    void deleteCountriesByIdTest() {

        long countryId = 1L;
        Country expectedCountry = buildCountry();

        Mockito.when(countryRepository.findById(countryId)).thenReturn(Optional.of(expectedCountry));

        // Act
        service.deleteById(countryId);

        //Assert
        Mockito.verify(countryRepository, Mockito.times(1)).deleteById(countryId);


    }

    /**
     * Test to update a country by its id
     **/
    @Test
    void updateCountriesByIdTest() {

        long countryId = 1L;
        CountryPostDto inputDto = new CountryPostDto();
        inputDto.setName("Updated Country");

        Country expectedCountry = buildCountry();
        Mockito.when(expectedCountry.getName()).thenReturn("Updated Country");

        Mockito.when(countryRepository.findById(countryId)).thenReturn(Optional.of(expectedCountry));
        Mockito.when(countryMapper.toCountry(inputDto)).thenReturn(expectedCountry);

        Country actualCountry = service.update(countryId, inputDto);

        assertEquals(expectedCountry, actualCountry);
        assertEquals("Updated Country", expectedCountry.getName());
        Mockito.verify(countryRepository).save(expectedCountry);

    }

    /**
     * Test to get a specific country by its id
     **/
    @Test
    void getCountryByIdTest() {

        long countryId = 1L;
        Country expectedCountry = buildCountry();

        Mockito.when(countryRepository.findById(countryId)).thenReturn(Optional.of(expectedCountry));

        service.getById(countryId);

        Mockito.verify(countryRepository,  Mockito.times(1)).findById(countryId);
    }
}