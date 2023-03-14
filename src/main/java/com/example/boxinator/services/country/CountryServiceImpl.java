package com.example.boxinator.services.country;

import com.example.boxinator.dtos.country.CountryMapper;
import com.example.boxinator.dtos.country.CountryPostDto;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.country.Country;
import com.example.boxinator.models.country.CountryTier;
import com.example.boxinator.repositories.country.CountryRepository;
import com.example.boxinator.repositories.country.CountryTierRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final CountryTierRepository countryTierRepository;
    private final CountryMapper countryMapper;

    public CountryServiceImpl(
            CountryRepository countryRepository,
            CountryTierRepository countryTierRepository,
            CountryMapper countryMapper
    ) {
        this.countryRepository = countryRepository;
        this.countryTierRepository = countryTierRepository;
        this.countryMapper = countryMapper;
    }

    @Override
    public Country create(CountryPostDto dto) {
        var country = countryMapper.toCountry(dto);
        if (countryRepository.findCountryByName(dto.getName()) != null) {
            throw new ApplicationException(
                    "A country with the name: " + dto.getName() + " exists already.",
                    HttpStatus.BAD_REQUEST
            );
        }

        setCountryTier(country);
        countryRepository.save(country);
        return country;
    }

    /**
     * 'Calculate' a tier for a country and set the tier object to the parameter country instance.
     * @param country to 'calculate' the tier for
     */
    private void setCountryTier(Country country) {
        List<CountryTier> tiers = countryTierRepository.getNonSourceTiers();
        CountryTier tier = tiers.get((int)(Math.random() * tiers.size()));
        country.setTier(tier);
    }

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {

        countryRepository.findById(id).orElseThrow(() -> new ApplicationException("Country wit this id does not exist", HttpStatus.NOT_FOUND));
        countryRepository.deleteById(id);
    }

    @Override
    public Country update(Long id, CountryPostDto dto) {
        Country country = countryRepository.findById(id).orElseThrow(() -> new ApplicationException(
                "A country with the id: " + id +  " Could not be found.",
                HttpStatus.NOT_FOUND
        ));
        country.setName(dto.getName());
        countryRepository.save(country);
        return country;
    }

    @Override
    public Country getById(Long id) {

        return countryRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("No country found with that id", HttpStatus.NOT_FOUND));

    }


    @Override
    public void delete(Long id) {
        countryRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Country wit this id does not exist", HttpStatus.NOT_FOUND));
        countryRepository.deleteById(id);
    }
}
