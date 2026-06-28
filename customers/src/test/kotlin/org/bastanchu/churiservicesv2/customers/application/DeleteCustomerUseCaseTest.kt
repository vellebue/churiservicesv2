package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.service.DeleteCustomerService
import org.bastanchu.churiservicesv2.customers.domain.Address
import org.bastanchu.churiservicesv2.customers.domain.Customer
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.CustomerNotFoundException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class DeleteCustomerUseCaseTest {

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @InjectMocks
    private lateinit var service: DeleteCustomerService

    @Test
    fun `should delete an existing customer`() {
        val customer = Customer(
            customerId = 1L,
            commercialName = "ACME",
            socialName = "ACME SA",
            vatNumber = "ESB12345678",
            customerAddress = Address(10L, "COMMERCIAL", "ACME SA", "Main St 1", "28001", "Madrid", "ES", "28"),
            socialAddress = Address(11L, "SOCIAL", "ACME SA", "Legal St 2", "28002", "Madrid", "ES", "28")
        )
        whenever(customerRepository.findById(1L)).thenReturn(customer)

        service.execute(1L)

        verify(customerRepository).deleteById(1L)
    }

    @Test
    fun `should throw when customer does not exist`() {
        whenever(customerRepository.findById(99L)).thenReturn(null)

        assertThrows(CustomerNotFoundException::class.java) {
            service.execute(99L)
        }
    }
}
