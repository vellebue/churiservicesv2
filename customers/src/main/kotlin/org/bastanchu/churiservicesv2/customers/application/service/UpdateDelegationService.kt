package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.customers.application.UpdateDelegationUseCase
import org.bastanchu.churiservicesv2.customers.application.command.UpdateDelegationCommand
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDelegationDto
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegation
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegationRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.DelegationNotFoundException
import org.bastanchu.churiservicesv2.customers.domain.exception.InvalidDelegationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateDelegationService(
    private val delegationRepository: CustomerDelegationRepository
) : UpdateDelegationUseCase {

    override fun execute(delegationId: Long, command: UpdateDelegationCommand): CustomerDelegationDto {
        val existing = delegationRepository.findById(delegationId)
            ?: throw DelegationNotFoundException(delegationId)

        val effectiveOrderId = if (command.orderId != null) {
            if (command.orderId != existing.orderId &&
                delegationRepository.existsByCustomerIdAndOrderId(
                    existing.customerId, command.orderId, excludeDelegationId = delegationId
                )
            ) {
                throw InvalidDelegationException(
                    "orderId ${command.orderId} is already registered for customer ${existing.customerId}"
                )
            }
            command.orderId
        } else {
            existing.orderId
        }

        val updated = CustomerDelegation(
            delegationId = delegationId,
            customerId = existing.customerId,
            orderId = effectiveOrderId,
            name = command.name,
            address = command.address.toDomain("LOCAL")
                .copy(addressId = existing.address.addressId),
            billingAddress = command.billingAddress?.toDomain("BILLING")
                ?.copy(addressId = existing.billingAddress?.addressId)
        )
        return delegationRepository.save(updated).toDto()
    }
}
