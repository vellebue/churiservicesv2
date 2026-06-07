package org.bastanchu.churiservicesv2.articles.application.dto

data class CountryRegionDto(
    val countryId: String,
    val regionId: String,
    val description: String,
    val localizedDescription: String
)
