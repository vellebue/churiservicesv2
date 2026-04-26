package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface ArticleJpaRepository : JpaRepository<ArticleJpaEntity, Long> {

    @Query(
        "SELECT DISTINCT a FROM ArticleJpaEntity a LEFT JOIN FETCH a.formats WHERE " +
        "(:articleId IS NULL OR a.articleId LIKE :articleId) AND " +
        "(:articleName IS NULL OR a.articleName LIKE :articleName) AND " +
        "(a.endValidityDate IS NULL OR a.endValidityDate >= :referenceDate)"
    )
    fun findByFilter(
        @Param("articleId") articleId: String?,
        @Param("articleName") articleName: String?,
        @Param("referenceDate") referenceDate: LocalDate
    ): List<ArticleJpaEntity>
}
