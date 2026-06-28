package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDto

interface GetCustomerByIdUseCase {
    fun execute(id: Long): CustomerDto
}
