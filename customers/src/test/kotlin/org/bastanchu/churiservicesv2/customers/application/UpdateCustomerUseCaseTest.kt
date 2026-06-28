package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.command.AddressCommand
import org.bastanchu.churiservicesv2.customers.application.command.UpdateCustomerCommand
import org.bastanchu.churiservicesv2.customers.application.service.UpdateCustomerService
import org.bastanchu.churiservicesv2.customers.domain.Address
import org.bastanchu.churiservicesv2.customers.domain.Customer
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.CustomerNotFoundException
import org.bastanchu.churiservicesv2.customers.domain.exception.InvalidCustomerException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class UpdateCustomerUseCaseTest {

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @InjectMocks
    private lateinit var service: UpdateCustomerService

    private val existing = Customer(
        customerId = 1L,
        commercialName = "ACME",
        socialName = "ACME SA",
        vatNumber = "ESB12345678",
        customerAddress = Address(10L, "COMMERCIAL", "ACME SA", "Main St 1", "28001", "Madrid", "ES", "28"),
        socialAddress = Address(11L, "SOCIAL", "ACME SA", "Legal St 2", "28002", "Madrid", "ES", "28")
    )

    private val command = UpdateCustomerCommand(
        commercialName = "ACME Updated",
        socialName = "ACME SA",
        vatNumber = "ESB99999999",
        customerAddress = AddressCommand("ACME SA", "Main St 1", "28001", "Madrid", "ES", "28"),
        socialAddress = AddressCommand("ACME SA", "Legal St 2", "28002", "Madrid", "ES", "28"),
        billingAddress = null
    )

    @Test
    fun `should update existing customer`() {
        whenever(customerRepository.findById(1L)).thenReturn(existing)
        whenever(customerRepository.existsByCountryAndVatNumber("ES", "ESB99999999", 1L)).thenReturn(false)
        whenever(customerRepository.save(any())).thenAnswer { it.arguments[0] as Customer }

        val result = service.execute(1L, command)

        assertEquals("ACME Updated", result.commercialName)
        assertEquals("ESB99999999", result.vatNumber)
    }

    @Test
    fun `should throw when customer does not exist`() {
        whenever(customerRepository.findById(99L)).thenReturn(null)

        assertThrows(CustomerNotFoundException::class.java) {
            service.execute(99L, command)
        }
    }

    @Test
    fun `should throw when vat number collides with another customer in same country`() {
        whenever(customerRepository.findById(1L)).thenReturn(existing)
        whenever(customerRepository.existsByCountryAndVatNumber("ES", "ESB99999999", 1L)).thenReturn(true)

        assertThrows(InvalidCustomerException::class.java) {
            service.execute(1L, command)
        }
    }
}
