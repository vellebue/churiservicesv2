package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.dto.CountryRegionDto

interface GetRegionsByCountryUseCase {
    fun execute(countryId: String): List<CountryRegionDto>
}
