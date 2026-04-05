package org.bastanchu.churiservicesv2.articles.domain

data class Article(
    val id: Long? = null,
    val articleId: String,
    val articleName: String,
    val formats: List<ArticleFormat>
)
