package com.example.boxinator.repositories.country;

import com.example.boxinator.models.country.Country;
import com.example.boxinator.models.country.CountryTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findCountryByName(String name);
}
