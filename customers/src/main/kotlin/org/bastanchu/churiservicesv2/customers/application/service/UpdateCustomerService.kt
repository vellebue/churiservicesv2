package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.customers.application.UpdateCustomerUseCase
import org.bastanchu.churiservicesv2.customers.application.command.UpdateCustomerCommand
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDto
import org.bastanchu.churiservicesv2.customers.domain.Customer
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.CustomerNotFoundException
import org.bastanchu.churiservicesv2.customers.domain.exception.InvalidCustomerException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdateCustomerService(
    private val customerRepository: CustomerRepository
) : UpdateCustomerUseCase {

    override fun execute(id: Long, command: UpdateCustomerCommand): CustomerDto {
        val existing = customerRepository.findById(id) ?: throw CustomerNotFoundException(id)

        val socialCountry = command.socialAddress.country
        if (customerRepository.existsByCountryAndVatNumber(socialCountry, command.vatNumber, excludeCustomerId = id)) {
            throw InvalidCustomerException(
                "VAT number '${command.vatNumber}' is already registered for country '$socialCountry'"
            )
        }

        val updated = Customer(
            customerId = id,
            commercialName = command.commercialName,
            socialName = command.socialName,
            vatNumber = command.vatNumber,
            customerAddress = command.customerAddress.toDomain("COMMERCIAL")
                .copy(addressId = existing.customerAddress.addressId),
            socialAddress = command.socialAddress.toDomain("SOCIAL")
                .copy(addressId = existing.socialAddress.addressId),
            billingAddress = command.billingAddress?.toDomain("BILLING")
                ?.copy(addressId = existing.billingAddress?.addressId),
            delegations = existing.delegations
        )
        return customerRepository.save(updated).toDto()
    }
}
