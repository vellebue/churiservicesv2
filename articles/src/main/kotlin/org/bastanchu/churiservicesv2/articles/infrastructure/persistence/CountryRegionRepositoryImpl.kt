package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.bastanchu.churiservicesv2.articles.domain.CountryRegion
import org.bastanchu.churiservicesv2.articles.domain.CountryRegionRepository
import org.springframework.stereotype.Repository

@Repository
class CountryRegionRepositoryImpl(
    private val jpaRepository: CountryRegionJpaRepository
) : CountryRegionRepository {

    override fun findByCountryId(countryId: String): List<CountryRegion> {
        return jpaRepository.findByCountryId(countryId).map { it.toDomain() }
    }

    private fun CountryRegionJpaEntity.toDomain(): CountryRegion = CountryRegion(
        countryId = countryId,
        regionId = regionId,
        description = description,
        regionKey = regionKey
    )
}
