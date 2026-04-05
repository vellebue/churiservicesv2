package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.GetArticleByIdUseCase
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.bastanchu.churiservicesv2.articles.domain.exception.ArticleNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetArticleByIdService(
    private val articleRepository: ArticleRepository
) : GetArticleByIdUseCase {

    override fun execute(id: Long): ArticleDto {
        val article = articleRepository.findById(id)
            ?: throw ArticleNotFoundException(id)
        return article.toDto()
    }
}
