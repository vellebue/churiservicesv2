package org.bastanchu.churiservicesv2.customers.application.service

import org.bastanchu.churiservicesv2.customers.application.DeleteCustomerUseCase
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.CustomerNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeleteCustomerService(
    private val customerRepository: CustomerRepository
) : DeleteCustomerUseCase {

    override fun execute(id: Long) {
        customerRepository.findById(id) ?: throw CustomerNotFoundException(id)
        customerRepository.deleteById(id)
    }
}
