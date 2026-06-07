package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.bastanchu.churiservicesv2.articles.domain.Country
import org.bastanchu.churiservicesv2.articles.domain.CountryRepository
import org.springframework.stereotype.Repository

@Repository
class CountryRepositoryImpl(
    private val jpaRepository: CountryJpaRepository
) : CountryRepository {

    override fun findAll(): List<Country> {
        return jpaRepository.findAll().map { it.toDomain() }
    }

    private fun CountryJpaEntity.toDomain(): Country = Country(
        countryId = countryId,
        description = description,
        countryKey = countryKey
    )
}
