package com.example.boxinator.services.country;

import com.example.boxinator.dtos.country.CountryPostDto;
import com.example.boxinator.models.country.Country;
import com.example.boxinator.services.shared.CrudService;
import org.springframework.stereotype.Service;

@Service
public interface CountryService extends CrudService<Country, CountryPostDto> {}
