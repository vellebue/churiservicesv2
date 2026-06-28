package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.command.UpdateCustomerCommand
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDto

interface UpdateCustomerUseCase {
    fun execute(id: Long, command: UpdateCustomerCommand): CustomerDto
}
