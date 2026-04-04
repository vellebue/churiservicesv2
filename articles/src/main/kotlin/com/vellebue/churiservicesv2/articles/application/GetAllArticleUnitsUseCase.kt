package com.vellebue.churiservicesv2.articles.application

interface GetAllArticleUnitsUseCase {
    fun execute(): List<ArticleUnitDto>
}
