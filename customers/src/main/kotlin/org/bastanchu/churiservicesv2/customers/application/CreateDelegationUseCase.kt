package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.command.CreateDelegationCommand
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDelegationDto

interface CreateDelegationUseCase {
    fun execute(customerId: Long, command: CreateDelegationCommand): CustomerDelegationDto
}
