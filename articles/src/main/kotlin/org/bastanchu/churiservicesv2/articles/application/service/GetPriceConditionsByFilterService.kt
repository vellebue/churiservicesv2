package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.GetPriceConditionsByFilterUseCase
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionFilterDto
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionFilter
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.common.application.pagination.SortSpec
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetPriceConditionsByFilterService(
    private val priceConditionRepository: PriceConditionRepository
) : GetPriceConditionsByFilterUseCase {

    private val sortSpec = SortSpec(
        allowedFields = setOf("conditionId", "subtypeId", "scope", "evaluation", "description"),
        default = emptyList(),
        tieBreaker = "conditionId"
    )

    override fun execute(filter: PriceConditionFilterDto, pageRequest: PageRequest): FilteredPage<PriceConditionDto> {
        val resolvedRequest = pageRequest.copy(sort = sortSpec.resolve(pageRequest.sort))
        val domainFilter = PriceConditionFilter(
            subtypeId = filter.subtypeId,
            scope = filter.scope,
            evaluation = filter.evaluation,
            descriptionPattern = filter.description,
            beginDate = filter.beginDate,
            endDate = filter.endDate
        )
        val page = priceConditionRepository.findByFilter(domainFilter, resolvedRequest)
        return FilteredPage(
            content = page.content.map { it.toDto() },
            offset = page.offset,
            limit = page.limit,
            hasMore = page.hasMore,
            sort = page.sort
        )
    }
}
