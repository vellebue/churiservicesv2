package com.vellebue.churiservicesv2.articles.application

interface GetArticleUnitBySymbolUseCase {
    fun execute(symbol: String): ArticleUnitDto?
}
