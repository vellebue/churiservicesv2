package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDelegationDto

interface GetDelegationByIdUseCase {
    fun execute(delegationId: Long): CustomerDelegationDto
}
