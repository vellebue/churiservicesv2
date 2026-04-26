package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.GetArticlesByFilterUseCase
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleFilterDto
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class GetArticlesByFilterService(
    private val articleRepository: ArticleRepository
) : GetArticlesByFilterUseCase {

    override fun execute(filter: ArticleFilterDto): List<ArticleDto> {
        return articleRepository.findByFilter(filter.articleId, filter.articleName, LocalDate.now())
            .map { it.toDto() }
    }
}
