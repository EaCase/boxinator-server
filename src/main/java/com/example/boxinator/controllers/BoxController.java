package com.example.boxinator.controllers;

import com.example.boxinator.dtos.box.BoxTierGetDto;
import com.example.boxinator.dtos.country.CountryGetDto;
import com.example.boxinator.models.box.BoxTier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "boxes")
public class BoxController {

    @GetMapping("/tiers")
    @Operation(summary = "Get all the available box tiers which can be used in shipments.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns a list of the box tier objects.",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = BoxTierGetDto.class))
            )}
    )
    public ResponseEntity<List<BoxTierGetDto>> getBoxTiers() {
        throw new RuntimeException("Not implemented.");
    }
}
