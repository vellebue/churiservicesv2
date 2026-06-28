package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.customers.application.GetCustomerByIdUseCase
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerDto
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.CustomerNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetCustomerByIdService(
    private val customerRepository: CustomerRepository
) : GetCustomerByIdUseCase {

    override fun execute(id: Long): CustomerDto {
        val customer = customerRepository.findById(id) ?: throw CustomerNotFoundException(id)
        return customer.toDto()
    }
}
