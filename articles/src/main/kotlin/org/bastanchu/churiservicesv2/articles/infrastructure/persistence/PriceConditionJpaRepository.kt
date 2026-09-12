package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface PriceConditionJpaRepository : JpaRepository<PriceConditionJpaEntity, Long> {

    @Query(
        "SELECT DISTINCT pc FROM PriceConditionJpaEntity pc LEFT JOIN FETCH pc.values v WHERE " +
            "(:subtypeId IS NULL OR pc.subtypeId = :subtypeId) AND " +
            "(:scope IS NULL OR pc.scope = :scope) AND " +
            "(:evaluation IS NULL OR pc.evaluation = :evaluation) AND " +
            "(:descriptionPattern IS NULL OR pc.description LIKE :descriptionPattern) AND " +
            "(:beginDate IS NULL OR v.beginDate = :beginDate) AND " +
            "(:endDate IS NULL OR v.endDate = :endDate)"
    )
    fun findByFilter(
        @Param("subtypeId") subtypeId: Long?,
        @Param("scope") scope: Char?,
        @Param("evaluation") evaluation: Char?,
        @Param("descriptionPattern") descriptionPattern: String?,
        @Param("beginDate") beginDate: LocalDate?,
        @Param("endDate") endDate: LocalDate?,
        pageable: Pageable
    ): List<PriceConditionJpaEntity>

    @Query(
        "SELECT DISTINCT pc FROM ArticleJpaEntity a JOIN a.priceConditions pc " +
            "LEFT JOIN FETCH pc.values WHERE a.id = :articleId"
    )
    fun findAllByArticleId(@Param("articleId") articleId: Long): List<PriceConditionJpaEntity>
}
