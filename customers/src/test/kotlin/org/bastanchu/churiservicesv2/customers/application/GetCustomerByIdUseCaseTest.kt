package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.service.GetCustomerByIdService
import org.bastanchu.churiservicesv2.customers.domain.Address
import org.bastanchu.churiservicesv2.customers.domain.Customer
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegation
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.CustomerNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetCustomerByIdUseCaseTest {

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @InjectMocks
    private lateinit var service: GetCustomerByIdService

    @Test
    fun `should return customer with delegations`() {
        val delegation = CustomerDelegation(
            delegationId = 100L,
            customerId = 1L,
            orderId = 0,
            name = "Madrid Branch",
            address = Address(20L, "LOCAL", "ACME Madrid", "Branch St 1", "28010", "Madrid", "ES", "28")
        )
        val customer = Customer(
            customerId = 1L,
            commercialName = "ACME",
            socialName = "ACME SA",
            vatNumber = "ESB12345678",
            customerAddress = Address(10L, "COMMERCIAL", "ACME SA", "Main St 1", "28001", "Madrid", "ES", "28"),
            socialAddress = Address(11L, "SOCIAL", "ACME SA", "Legal St 2", "28002", "Madrid", "ES", "28"),
            delegations = listOf(delegation)
        )
        whenever(customerRepository.findById(1L)).thenReturn(customer)

        val result = service.execute(1L)

        assertEquals(1L, result.customerId)
        assertEquals(1, result.delegations.size)
        assertEquals("Madrid Branch", result.delegations[0].name)
    }

    @Test
    fun `should throw when customer does not exist`() {
        whenever(customerRepository.findById(99L)).thenReturn(null)

        assertThrows(CustomerNotFoundException::class.java) {
            service.execute(99L)
        }
    }
}
