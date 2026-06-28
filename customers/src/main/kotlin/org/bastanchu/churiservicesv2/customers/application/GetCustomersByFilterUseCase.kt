package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDto
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerFilterDto

interface GetCustomersByFilterUseCase {
    fun execute(filter: CustomerFilterDto, pageRequest: PageRequest): FilteredPage<CustomerDto>
}
