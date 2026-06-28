package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.command.UpdateDelegationCommand
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDelegationDto

interface UpdateDelegationUseCase {
    fun execute(delegationId: Long, command: UpdateDelegationCommand): CustomerDelegationDto
}
