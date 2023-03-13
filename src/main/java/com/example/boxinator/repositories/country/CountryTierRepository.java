package com.example.boxinator.repositories.country;

import com.example.boxinator.models.country.CountryTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryTierRepository extends JpaRepository<CountryTier, Long> {
    @Query(nativeQuery = true,
            value = "SELECT id, name, shipping_multiplier FROM country_tier WHERE name = 'Source'")
    CountryTier getSourceTier();

    @Query(nativeQuery = true,
            value = "SELECT id, name, shipping_multiplier FROM country_tier WHERE NOT name = 'Source'")
    List<CountryTier> getNonSourceTiers();
}
