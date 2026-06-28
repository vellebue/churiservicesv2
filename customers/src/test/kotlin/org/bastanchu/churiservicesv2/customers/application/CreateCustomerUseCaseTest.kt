package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.command.AddressCommand
import org.bastanchu.churiservicesv2.customers.application.command.CreateCustomerCommand
import org.bastanchu.churiservicesv2.customers.application.service.CreateCustomerService
import org.bastanchu.churiservicesv2.customers.domain.Address
import org.bastanchu.churiservicesv2.customers.domain.Customer
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
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
class CreateCustomerUseCaseTest {

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @InjectMocks
    private lateinit var service: CreateCustomerService

    private val customerAddress = AddressCommand("ACME SA", "Main St 1", "28001", "Madrid", "ES", "28")
    private val socialAddress = AddressCommand("ACME SA", "Legal St 2", "28002", "Madrid", "ES", "28")

    private val command = CreateCustomerCommand(
        commercialName = "ACME",
        socialName = "ACME SA",
        vatNumber = "ESB12345678",
        customerAddress = customerAddress,
        socialAddress = socialAddress,
        billingAddress = null
    )

    @Test
    fun `should create customer when vat number is not already taken`() {
        whenever(customerRepository.existsByCountryAndVatNumber("ES", "ESB12345678")).thenReturn(false)
        whenever(customerRepository.save(any())).thenReturn(
            Customer(
                customerId = 7L,
                commercialName = "ACME",
                socialName = "ACME SA",
                vatNumber = "ESB12345678",
                customerAddress = Address(1L, "COMMERCIAL", "ACME SA", "Main St 1", "28001", "Madrid", "ES", "28"),
                socialAddress = Address(2L, "SOCIAL", "ACME SA", "Legal St 2", "28002", "Madrid", "ES", "28")
            )
        )

        val result = service.execute(command)

        assertEquals(7L, result.customerId)
        assertEquals("ESB12345678", result.vatNumber)
        assertEquals("COMMERCIAL", result.customerAddress.addressType)
        assertEquals("SOCIAL", result.socialAddress.addressType)
    }

    @Test
    fun `should throw when vat number is already registered for the social country`() {
        whenever(customerRepository.existsByCountryAndVatNumber("ES", "ESB12345678")).thenReturn(true)

        assertThrows(InvalidCustomerException::class.java) {
            service.execute(command)
        }
    }
}
