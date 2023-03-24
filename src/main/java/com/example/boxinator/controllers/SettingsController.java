package com.example.boxinator.controllers;

import com.example.boxinator.dtos.country.CountryGetDto;
import com.example.boxinator.dtos.country.CountryMapper;
import com.example.boxinator.dtos.country.CountryPostDto;
import com.example.boxinator.services.country.CountryService;
import com.example.boxinator.services.country.CountryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "settings")
public class SettingsController {
    private final CountryService countryService;
    private final CountryMapper countryMapper;

    public SettingsController(CountryServiceImpl countryService, CountryMapper countryMapper) {
        this.countryService = countryService;
        this.countryMapper = countryMapper;
    }

    @GetMapping("/countries")
    @Operation(summary = "Get all the countries.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns a list of the country objects.",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CountryGetDto.class))
            )}
    )
    public ResponseEntity<List<CountryGetDto>> getCountries() {
        var countries = countryService.getAll().stream().map(countryMapper::toGetDto).toList();
        return ResponseEntity.ok().body(countries);
    }

    @PostMapping("/countries")
    @Operation(summary = "Add a new country by providing its name.")
    @ApiResponse(
            responseCode = "201",
            description = "Returns the created country.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CountryGetDto.class)
            )}
    )
    public ResponseEntity<CountryGetDto> addCountry(@RequestBody CountryPostDto body) {
        var createdCountry = countryMapper.toGetDto(countryService.create(body));
        return ResponseEntity.ok().body(createdCountry);
    }

    @PutMapping("/countries/{id}")
    @Operation(summary = "Edit the name of a country.")
    @ApiResponse(
            responseCode = "204",
            description = "Returns the country with the new name.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CountryGetDto.class)
            )}
    )
    public ResponseEntity<CountryGetDto> updateCountry(@PathVariable Long id, @RequestBody CountryPostDto body) {
        var editedCountry = countryMapper.toGetDto(countryService.update(id, body));
        return ResponseEntity.ok().body(editedCountry);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a country")
    @ApiResponse(responseCode = "200",
            description = "Id of the deleted country.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class))})
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<Long> delete(@PathVariable long id) {
        countryService.deleteById(id);
        return ResponseEntity.ok().body(id);
    }
}
