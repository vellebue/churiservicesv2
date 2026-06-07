package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "COUNTRIES")
class CountryJpaEntity(
    @Id
    @Column(name = "country_id", length = 2)
    val countryId: String = "",

    @Column(name = "description", nullable = false, length = 256)
    val description: String = "",

    @Column(name = "country_key", nullable = false, length = 20)
    val countryKey: String = ""
)
