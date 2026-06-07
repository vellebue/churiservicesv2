package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.dto.CountryDto

interface GetAllCountriesUseCase {
    fun execute(): List<CountryDto>
}
