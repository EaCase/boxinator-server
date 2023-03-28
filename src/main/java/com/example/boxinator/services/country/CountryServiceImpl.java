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
    private final CountryMapper countryMapper;

    public CountryServiceImpl(
            CountryRepository countryRepository,
            CountryMapper countryMapper
    ) {
        this.countryRepository = countryRepository;
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

        countryRepository.save(country);
        return country;
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
                "A country with the id: " + id + " Could not be found.",
                HttpStatus.NOT_FOUND
        ));

        country.setShippingMultiplier(dto.getShippingMultiplier());
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
