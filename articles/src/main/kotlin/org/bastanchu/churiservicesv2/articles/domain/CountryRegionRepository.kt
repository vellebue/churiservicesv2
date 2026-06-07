package org.bastanchu.churiservicesv2.articles.domain

interface CountryRegionRepository {
    fun findByCountryId(countryId: String): List<CountryRegion>
}
