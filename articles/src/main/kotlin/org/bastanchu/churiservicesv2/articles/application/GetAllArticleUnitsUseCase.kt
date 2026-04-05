package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.dto.ArticleUnitDto

interface GetAllArticleUnitsUseCase {
    fun execute(): List<ArticleUnitDto>
}
