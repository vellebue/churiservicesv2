package com.vellebue.churiservicesv2.articles.domain

interface ArticleUnitRepository {
    fun findBySymbol(symbol: String): ArticleUnit?
    fun findAll(): List<ArticleUnit>
}
