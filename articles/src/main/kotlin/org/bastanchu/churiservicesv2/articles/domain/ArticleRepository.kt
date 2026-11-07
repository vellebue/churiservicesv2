package org.bastanchu.churiservicesv2.articles.domain

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import java.time.LocalDate

interface ArticleRepository {
    fun save(article: Article): Article
    fun findById(id: Long): Article?
    fun deleteById(id: Long)
    fun findByFilter(
        articleIdPattern: String?,
        articleNamePattern: String?,
        referenceDate: LocalDate,
        pageRequest: PageRequest
    ): FilteredPage<Article>
}
