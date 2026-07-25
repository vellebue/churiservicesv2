package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PriceConditionJpaRepository : JpaRepository<PriceConditionJpaEntity, Long> {

    @Query(
        "SELECT DISTINCT pc FROM PriceConditionJpaEntity pc LEFT JOIN FETCH pc.values WHERE " +
        "(:conditionType IS NULL OR pc.conditionType = :conditionType) AND " +
        "(:description IS NULL OR pc.description LIKE :description)"
    )
    fun findByFilter(
        @Param("conditionType") conditionType: String?,
        @Param("description") description: String?,
        pageable: Pageable
    ): List<PriceConditionJpaEntity>
}
