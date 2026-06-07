package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface CountryRegionJpaRepository : JpaRepository<CountryRegionJpaEntity, CountryRegionId> {
    fun findByCountryId(countryId: String): List<CountryRegionJpaEntity>
}
