package com.example.boxinator.dtos.fee;

import com.example.boxinator.models.fee.Fee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class FeeMapper {
    public abstract FeeGetDto toFeeDto(Fee fee);
}
