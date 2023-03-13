package com.example.boxinator.dtos.country;

import com.example.boxinator.models.country.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CountryMapper {
    public abstract Country toCountry(CountryPostDto dto);

    public abstract CountryGetDto toGetDto(Country country);
}
