package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.GetPriceConditionsByFilterUseCase
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionFilterDto
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetPriceConditionsByFilterService(
    private val priceConditionRepository: PriceConditionRepository
) : GetPriceConditionsByFilterUseCase {

    override fun execute(filter: PriceConditionFilterDto, pageRequest: PageRequest): FilteredPage<PriceConditionDto> {
        val descriptionPattern = filter.description?.replace('*', '%')
        val result = priceConditionRepository.findByFilter(
            conditionType = filter.conditionType,
            descriptionPattern = descriptionPattern,
            pageRequest = pageRequest
        )
        return FilteredPage(
            content = result.content.map { it.toDto() },
            offset = result.offset,
            limit = result.limit,
            hasMore = result.hasMore,
            sort = result.sort
        )
    }
}
