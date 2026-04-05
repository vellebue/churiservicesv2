package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.command.UpdateArticleCommand
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto

interface UpdateArticleUseCase {
    fun execute(id: Long, command: UpdateArticleCommand): ArticleDto
}
