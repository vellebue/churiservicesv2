package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.bastanchu.churiservicesv2.articles.domain.ArticleUnit
import org.bastanchu.churiservicesv2.articles.domain.ArticleUnitRepository
import org.springframework.stereotype.Repository

@Repository
class ArticleUnitRepositoryImpl(
    private val jpaRepository: ArticleUnitJpaRepository
) : ArticleUnitRepository {

    override fun findBySymbol(symbol: String): ArticleUnit? {
        return jpaRepository.findById(symbol).orElse(null)?.toDomain()
    }

    override fun findAll(): List<ArticleUnit> {
        return jpaRepository.findAll().map { it.toDomain() }
    }

    private fun ArticleUnitJpaEntity.toDomain(): ArticleUnit {
        return ArticleUnit(
            symbol = symbol,
            description = description,
            translationKey = translationKey
        )
    }
}
