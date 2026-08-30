package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface PriceConditionSubtypeJpaRepository : JpaRepository<PriceConditionSubtypeJpaEntity, Long> {
    fun findByConditionTypeAndConditionSubtype(conditionType: String, conditionSubtype: String): PriceConditionSubtypeJpaEntity?
    fun findByConditionType(conditionType: String): List<PriceConditionSubtypeJpaEntity>
}
