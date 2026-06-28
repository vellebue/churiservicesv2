package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.common.application.pagination.SortSpec
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.common.domain.pagination.SortCriterion
import org.bastanchu.churiservicesv2.common.domain.pagination.SortDirection
import org.bastanchu.churiservicesv2.customers.application.GetCustomersByFilterUseCase
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDto
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerFilterDto
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetCustomersByFilterService(
    private val customerRepository: CustomerRepository
) : GetCustomersByFilterUseCase {

    private val sortSpec = SortSpec(
        allowedFields = setOf("commercialName", "socialName", "vatNumber", "customerId"),
        default = listOf(SortCriterion("commercialName", SortDirection.ASC)),
        tieBreaker = "customerId"
    )

    override fun execute(filter: CustomerFilterDto, pageRequest: PageRequest): FilteredPage<CustomerDto> {
        val effective = pageRequest.copy(sort = sortSpec.resolve(pageRequest.sort))
        val page = customerRepository.findByFilter(
            commercialNamePattern = filter.commercialName,
            socialNamePattern = filter.socialName,
            vatNumberPattern = filter.vatNumber,
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
