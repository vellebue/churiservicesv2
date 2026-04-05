package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleFilterDto

interface GetArticlesByFilterUseCase {
    fun execute(filter: ArticleFilterDto): List<ArticleDto>
}
