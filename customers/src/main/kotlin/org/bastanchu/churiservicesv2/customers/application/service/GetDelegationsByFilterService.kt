package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.common.application.pagination.SortSpec
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.common.domain.pagination.SortCriterion
import org.bastanchu.churiservicesv2.common.domain.pagination.SortDirection
import org.bastanchu.churiservicesv2.customers.application.GetDelegationsByFilterUseCase
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDelegationDto
import org.bastanchu.churiservicesv2.customers.application.dto.DelegationFilterDto
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetDelegationsByFilterService(
    private val delegationRepository: CustomerDelegationRepository
) : GetDelegationsByFilterUseCase {

    private val sortSpec = SortSpec(
        allowedFields = setOf("name", "orderId", "delegationId"),
        default = listOf(SortCriterion("name", SortDirection.ASC)),
        tieBreaker = "delegationId"
    )

    override fun execute(filter: DelegationFilterDto, pageRequest: PageRequest): FilteredPage<CustomerDelegationDto> {
        val effective = pageRequest.copy(sort = sortSpec.resolve(pageRequest.sort))
        val page = delegationRepository.findByFilter(
            namePattern = filter.name,
            pageRequest = effective
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
