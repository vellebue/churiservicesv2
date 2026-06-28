package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDelegationDto
import org.bastanchu.churiservicesv2.customers.application.dto.DelegationFilterDto

interface GetDelegationsByFilterUseCase {
    fun execute(filter: DelegationFilterDto, pageRequest: PageRequest): FilteredPage<CustomerDelegationDto>
}
