package org.bastanchu.churiservicesv2.articles.infrastructure.web

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.bastanchu.churiservicesv2.articles.application.GetAllCountriesUseCase
import org.bastanchu.churiservicesv2.articles.application.GetRegionsByCountryUseCase
import org.bastanchu.churiservicesv2.articles.application.dto.CountryDto
import org.bastanchu.churiservicesv2.articles.application.dto.CountryRegionDto
import org.bastanchu.churiservicesv2.common.infrastructure.logging.LogEntry
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/countries")
@Tag(name = "Countries", description = "Operations for retrieving countries and their regions")
@LogEntry
class CountryController(
    private val getAllCountriesUseCase: GetAllCountriesUseCase,
    private val getRegionsByCountryUseCase: GetRegionsByCountryUseCase
) {

    @GetMapping
    @Operation(summary = "Get all countries", description = "Returns the full list of countries with localized names")
    @ApiResponse(responseCode = "200", description = "List of countries retrieved successfully")
    fun getAllCountries(): ResponseEntity<List<CountryDto>> {
        return ResponseEntity.ok(getAllCountriesUseCase.execute())
    }

    @GetMapping("/country/{countryId}/regions")
    @Operation(summary = "Get regions by country", description = "Returns the regions of a given country with localized names")
    @ApiResponse(responseCode = "200", description = "List of regions retrieved successfully")
    fun getRegionsByCountry(@PathVariable countryId: String): ResponseEntity<List<CountryRegionDto>> {
        return ResponseEntity.ok(getRegionsByCountryUseCase.execute(countryId))
    }
}
