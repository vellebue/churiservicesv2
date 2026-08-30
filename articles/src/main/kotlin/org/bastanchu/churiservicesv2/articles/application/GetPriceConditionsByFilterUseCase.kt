package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionFilterDto
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest

interface GetPriceConditionsByFilterUseCase {
    fun execute(filter: PriceConditionFilterDto, pageRequest: PageRequest): FilteredPage<PriceConditionDto>
}
