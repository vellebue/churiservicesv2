package org.bastanchu.churiservicesv2.articles.domain

import java.time.LocalDate

interface ArticleRepository {
    fun save(article: Article): Article
    fun findById(id: Long): Article?
    fun deleteById(id: Long)
    fun findByFilter(
        articleIdPattern: String?,
        articleNamePattern: String?,
        referenceDate: LocalDate
    ): List<Article>
}
