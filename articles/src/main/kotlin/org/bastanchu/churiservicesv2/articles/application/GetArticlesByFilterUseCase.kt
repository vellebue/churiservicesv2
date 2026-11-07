package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.dto.ArticleDto
import org.bastanchu.churiservicesv2.articles.application.dto.ArticleFilterDto
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest

interface GetArticlesByFilterUseCase {
    fun execute(filter: ArticleFilterDto, pageRequest: PageRequest): FilteredPage<ArticleDto>
}
