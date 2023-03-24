package com.example.boxinator.services.box;

import com.example.boxinator.models.box.BoxTier;
import com.example.boxinator.repositories.box.BoxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BoxServiceImplTest {

    private BoxServiceImpl boxService;
    private BoxRepository boxRepository;
    @BeforeEach
    void setUp() {
        boxRepository = Mockito.mock(BoxRepository.class);

        boxService = new BoxServiceImpl(
                boxRepository
        );
    }

    /**
     * Used to create a mystery box. Used in multiple tests
     * used for the purpose of DRY
     **/
    BoxTier buildBoxTier() {
        BoxTier boxTier = Mockito.mock(BoxTier.class);
        Mockito.when(boxTier.getId()).thenReturn(3L);
        Mockito.when(boxTier.getName()).thenReturn("Deluxe");
        Mockito.when(boxTier.getWeight()).thenReturn(5F);

        return boxTier;
    }
    /**
     * Testing to get all box tiers
     **/
    @Test
    void getAllBoxTiersTest() {

        BoxTier boxTier = Mockito.mock(BoxTier.class);
        Mockito.when(boxTier.getId()).thenReturn(3L);
        Mockito.when(boxTier.getName()).thenReturn("Deluxe");
        Mockito.when(boxTier.getWeight()).thenReturn(5F);

        List<BoxTier> expectedBoxTiers = new ArrayList<>();
        expectedBoxTiers.add(boxTier);

        Mockito.when(boxRepository.findAll()).thenReturn(expectedBoxTiers);

        List<BoxTier> actualBoxTiers = boxService.getAllBoxTiers();

        assertEquals(expectedBoxTiers,actualBoxTiers);
    }

    /**
     * Get a specific box by its id
     **/
    @Test
    void getByBoxTierIdTest() {

        long boxTierId = 3L;
        BoxTier expectedBoxTier = buildBoxTier();

        Mockito.when(boxRepository.findById(boxTierId)).thenReturn(Optional.of(expectedBoxTier));

        boxService.getById(boxTierId);


        Mockito.verify(boxRepository, Mockito.times(1)).findById(boxTierId);
    }
}