package com.example.boxinator.dtos.account;

import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.country.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    @Mapping(source = "country", target = "countryId", qualifiedByName = "countryToId")
    public abstract AccountGetDto toDto(Account account);

    @Named("countryToId")
    public Long countryToId(Country country) {
        return country.getId();
    }
}
