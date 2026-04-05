package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.command.CreateArticleCommand
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto

interface CreateArticleUseCase {
    fun execute(command: CreateArticleCommand): ArticleDto
}
