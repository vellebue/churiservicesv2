package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.GetArticlesByFilterUseCase
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleFilterDto
import org.bastanchu.churiservicesv2.articles.domain.ArticleRepository
import org.bastanchu.churiservicesv2.common.application.pagination.SortSpec
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class GetArticlesByFilterService(
    private val articleRepository: ArticleRepository
) : GetArticlesByFilterUseCase {

    private val sortSpec = SortSpec(
        allowedFields = setOf("articleId", "articleName"),
        default = emptyList(),
        tieBreaker = "articleId"
    )

    override fun execute(filter: ArticleFilterDto, pageRequest: PageRequest): FilteredPage<ArticleDto> {
        val resolvedRequest = pageRequest.copy(sort = sortSpec.resolve(pageRequest.sort))
        val page = articleRepository.findByFilter(
            articleIdPattern = filter.articleId,
            articleNamePattern = filter.articleName,
            referenceDate = LocalDate.now(),
            pageRequest = resolvedRequest
        )
        return FilteredPage(
            content = page.content.map { it.toDto() },
            offset = page.offset,
            limit = page.limit,
            hasMore = page.hasMore,
            sort = page.sort
        )
    }
}
