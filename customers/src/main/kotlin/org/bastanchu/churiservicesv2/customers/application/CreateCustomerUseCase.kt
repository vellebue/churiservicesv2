package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.command.CreateCustomerCommand
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDto

interface CreateCustomerUseCase {
    fun execute(command: CreateCustomerCommand): CustomerDto
}
