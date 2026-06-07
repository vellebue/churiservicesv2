package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import java.io.Serializable

@Entity
@Table(name = "COUNTRY_REGIONS")
@IdClass(CountryRegionId::class)
class CountryRegionJpaEntity(
    @Id
    @Column(name = "country_id", length = 2)
    val countryId: String = "",

    @Id
    @Column(name = "region_id", length = 20)
    val regionId: String = "",

    @Column(name = "description", nullable = false, length = 256)
    val description: String = "",

    @Column(name = "region_key", nullable = false, length = 50)
    val regionKey: String = ""
)

data class CountryRegionId(
    val countryId: String = "",
    val regionId: String = ""
) : Serializable
