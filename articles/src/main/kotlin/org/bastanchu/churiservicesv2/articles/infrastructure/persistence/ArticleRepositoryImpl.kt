package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.bastanchu.churiservicesv2.articles.domain.Article
import org.bastanchu.churiservicesv2.articles.domain.ArticleFormat
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ArticleRepositoryImpl(
    private val jpaRepository: ArticleJpaRepository
) : ArticleRepository {

    override fun save(article: Article): Article {
        val jpaEntity = if (article.id != null) {
            val existing = jpaRepository.findById(article.id).orElseThrow()
            existing.articleId = article.articleId
            existing.articleName = article.articleName
            existing.beginValidityDate = article.beginValidityDate
            existing.endValidityDate = article.endValidityDate
            existing.formats.clear()
            article.formats.forEach { fmt ->
                existing.formats.add(fmt.toJpaEntity(existing))
            }
            existing
        } else {
            val newEntity = ArticleJpaEntity(
                articleId = article.articleId,
                articleName = article.articleName,
                beginValidityDate = article.beginValidityDate,
                endValidityDate = article.endValidityDate
            )
            article.formats.forEach { fmt ->
                newEntity.formats.add(fmt.toJpaEntity(newEntity))
            }
            newEntity
        }

        val saved = jpaRepository.save(jpaEntity)
        return saved.toDomain()
    }

    override fun findById(id: Long): Article? {
        return jpaRepository.findById(id).orElse(null)?.toDomain()
    }

    override fun deleteById(id: Long) {
        jpaRepository.deleteById(id)
    }

    override fun findByFilter(
        articleIdPattern: String?,
        articleNamePattern: String?,
        referenceDate: LocalDate
    ): List<Article> {
        val sqlArticleId = articleIdPattern?.replace('*', '%')
        val sqlArticleName = articleNamePattern?.replace('*', '%')
        return jpaRepository.findByFilter(sqlArticleId, sqlArticleName, referenceDate)
            .map { it.toDomain() }
    }

    private fun ArticleJpaEntity.toDomain(): Article = Article(
        id = id,
        articleId = articleId,
        articleName = articleName,
        beginValidityDate = beginValidityDate,
        endValidityDate = endValidityDate,
        formats = formats.map { it.toDomain() }
    )

    private fun ArticleFormatJpaEntity.toDomain(): ArticleFormat = ArticleFormat(
        id = id,
        description = description,
        referenceUnit = referenceUnit,
        eanCode = eanCode,
        eanType = eanType,
        saleUnit = saleUnit,
        conversionFactor = conversionFactor,
        articleUnitId = articleUnitId
    )

    private fun ArticleFormat.toJpaEntity(parent: ArticleJpaEntity): ArticleFormatJpaEntity =
        ArticleFormatJpaEntity(
            article = parent,
            description = description,
            referenceUnit = referenceUnit,
            eanCode = eanCode,
            eanType = eanType,
            saleUnit = saleUnit,
            conversionFactor = conversionFactor,
            articleUnitId = articleUnitId
        )
}
