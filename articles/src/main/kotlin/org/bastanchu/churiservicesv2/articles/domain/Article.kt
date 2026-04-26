package org.bastanchu.churiservicesv2.articles.domain

import java.time.LocalDate

data class Article(
    val id: Long? = null,
    val articleId: String,
    val articleName: String,
    val beginValidityDate: LocalDate,
    val endValidityDate: LocalDate? = null,
    val formats: List<ArticleFormat>
)
