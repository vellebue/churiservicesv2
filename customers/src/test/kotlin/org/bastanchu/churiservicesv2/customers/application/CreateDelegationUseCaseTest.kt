package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.command.AddressCommand
import org.bastanchu.churiservicesv2.customers.application.command.CreateDelegationCommand
import org.bastanchu.churiservicesv2.customers.application.service.CreateDelegationService
import org.bastanchu.churiservicesv2.customers.domain.Address
import org.bastanchu.churiservicesv2.customers.domain.Customer
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegation
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegationRepository
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.CustomerNotFoundException
import org.bastanchu.churiservicesv2.customers.domain.exception.InvalidDelegationException
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
class CreateDelegationUseCaseTest {

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @Mock
    private lateinit var delegationRepository: CustomerDelegationRepository

    @InjectMocks
    private lateinit var service: CreateDelegationService

    private val customer = Customer(
        customerId = 1L,
        commercialName = "ACME",
        socialName = "ACME SA",
        vatNumber = "ESB12345678",
        customerAddress = Address(10L, "COMMERCIAL", "ACME SA", "Main St 1", "28001", "Madrid", "ES", "28"),
        socialAddress = Address(11L, "SOCIAL", "ACME SA", "Legal St 2", "28002", "Madrid", "ES", "28")
    )

    private val command = CreateDelegationCommand(
        orderId = null,
        name = "Madrid Branch",
        address = AddressCommand("ACME Madrid", "Branch St 1", "28010", "Madrid", "ES", "28"),
        billingAddress = null
    )

    private fun persisted(arg: CustomerDelegation): CustomerDelegation = arg.copy(
        delegationId = 50L,
        address = arg.address.copy(addressId = 20L),
        billingAddress = arg.billingAddress?.copy(addressId = 21L)
    )

    @Test
    fun `should auto-assign orderId when not provided`() {
        whenever(customerRepository.findById(1L)).thenReturn(customer)
        whenever(delegationRepository.maxOrderIdForCustomer(1L)).thenReturn(4)
        whenever(delegationRepository.save(any())).thenAnswer {
            persisted(it.arguments[0] as CustomerDelegation)
        }

        val result = service.execute(1L, command)

        assertEquals(5, result.orderId)
        assertEquals("LOCAL", result.address.addressType)
    }

    @Test
    fun `should start orderId at 0 when customer has no delegations yet`() {
        whenever(customerRepository.findById(1L)).thenReturn(customer)
        whenever(delegationRepository.maxOrderIdForCustomer(1L)).thenReturn(null)
        whenever(delegationRepository.save(any())).thenAnswer {
            persisted(it.arguments[0] as CustomerDelegation)
        }

        val result = service.execute(1L, command)

        assertEquals(0, result.orderId)
    }

    @Test
    fun `should throw when supplied orderId already exists for customer`() {
        whenever(customerRepository.findById(1L)).thenReturn(customer)
        whenever(delegationRepository.existsByCustomerIdAndOrderId(1L, 7)).thenReturn(true)

        assertThrows(InvalidDelegationException::class.java) {
            service.execute(1L, command.copy(orderId = 7))
        }
    }

    @Test
    fun `should throw when customer does not exist`() {
        whenever(customerRepository.findById(99L)).thenReturn(null)

        assertThrows(CustomerNotFoundException::class.java) {
            service.execute(99L, command)
        }
    }
}
