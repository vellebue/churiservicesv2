package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto

interface GetArticleByIdUseCase {
    fun execute(id: Long): ArticleDto
}
