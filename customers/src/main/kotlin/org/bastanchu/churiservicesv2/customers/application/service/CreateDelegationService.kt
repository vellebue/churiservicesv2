package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.customers.application.CreateDelegationUseCase
import org.bastanchu.churiservicesv2.customers.application.command.CreateDelegationCommand
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDelegationDto
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegation
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegationRepository
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.CustomerNotFoundException
import org.bastanchu.churiservicesv2.customers.domain.exception.InvalidDelegationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateDelegationService(
    private val customerRepository: CustomerRepository,
    private val delegationRepository: CustomerDelegationRepository
) : CreateDelegationUseCase {

    override fun execute(customerId: Long, command: CreateDelegationCommand): CustomerDelegationDto {
        customerRepository.findById(customerId) ?: throw CustomerNotFoundException(customerId)

        val orderId = if (command.orderId != null) {
            if (delegationRepository.existsByCustomerIdAndOrderId(customerId, command.orderId)) {
                throw InvalidDelegationException(
                    "orderId ${command.orderId} is already registered for customer $customerId"
                )
            }
            command.orderId
        } else {
            (delegationRepository.maxOrderIdForCustomer(customerId) ?: -1) + 1
        }

        val delegation = CustomerDelegation(
            customerId = customerId,
            orderId = orderId,
            name = command.name,
            address = command.address.toDomain("LOCAL"),
            billingAddress = command.billingAddress?.toDomain("BILLING")
        )
        return delegationRepository.save(delegation).toDto()
    }
}
