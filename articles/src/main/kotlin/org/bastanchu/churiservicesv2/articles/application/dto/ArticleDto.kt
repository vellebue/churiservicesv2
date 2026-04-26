package org.bastanchu.churiservicesv2.articles.application.dto

import java.time.LocalDate

data class ArticleDto(
    val id: Long,
    val articleId: String,
    val articleName: String,
    val beginValidityDate: LocalDate,
    val endValidityDate: LocalDate? = null,
    val formats: List<ArticleFormatDto>
)
