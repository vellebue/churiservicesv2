package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "C_PRICE_CONDITION_TYPES")
class PriceConditionTypeJpaEntity(
    @Id
    @Column(name = "condition_type", length = 4)
    val conditionType: String = "",

    @Column(name = "description", nullable = false, length = 256)
    val description: String = "",

    @Column(name = "condition_key", nullable = false, length = 100)
    val conditionKey: String = ""
)
