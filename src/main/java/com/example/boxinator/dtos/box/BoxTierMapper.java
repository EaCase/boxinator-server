package com.example.boxinator.dtos.box;

import com.example.boxinator.models.box.BoxTier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class BoxTierMapper {
    public abstract BoxTierGetDto toDto(BoxTier boxTier);
}
