package org.bastanchu.churiservicesv2.articles.application.dto

data class ArticleDto(
    val id: Long,
    val articleId: String,
    val articleName: String,
    val formats: List<ArticleFormatDto>
)
