package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.customers.application.CreateCustomerUseCase
import org.bastanchu.churiservicesv2.customers.application.command.CreateCustomerCommand
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDto
import org.bastanchu.churiservicesv2.customers.domain.Customer
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.InvalidCustomerException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateCustomerService(
    private val customerRepository: CustomerRepository
) : CreateCustomerUseCase {

    override fun execute(command: CreateCustomerCommand): CustomerDto {
        val socialCountry = command.socialAddress.country
        if (customerRepository.existsByCountryAndVatNumber(socialCountry, command.vatNumber)) {
            throw InvalidCustomerException(
                "VAT number '${command.vatNumber}' is already registered for country '$socialCountry'"
            )
        }

        val customer = Customer(
            commercialName = command.commercialName,
            socialName = command.socialName,
            vatNumber = command.vatNumber,
            customerAddress = command.customerAddress.toDomain("COMMERCIAL"),
            socialAddress = command.socialAddress.toDomain("SOCIAL"),
            billingAddress = command.billingAddress?.toDomain("BILLING")
        )
        return customerRepository.save(customer).toDto()
    }
}
