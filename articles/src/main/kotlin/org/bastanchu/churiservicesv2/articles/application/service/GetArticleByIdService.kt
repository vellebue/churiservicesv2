package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.GetArticleByIdUseCase
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.exception.ArticleNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetArticleByIdService(
    private val articleRepository: ArticleRepository,
    private val priceConditionRepository: PriceConditionRepository
) : GetArticleByIdUseCase {

    override fun execute(id: Long): ArticleDto {
        val article = articleRepository.findById(id)
            ?: throw ArticleNotFoundException(id)
        val priceConditions = priceConditionRepository.findAllByArticleId(id)
            .map { it.toDto() }
        return article.toDto(priceConditions)
    }
}
