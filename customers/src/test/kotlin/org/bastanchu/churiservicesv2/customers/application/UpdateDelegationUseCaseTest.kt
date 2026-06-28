package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.command.AddressCommand
import org.bastanchu.churiservicesv2.customers.application.command.UpdateDelegationCommand
import org.bastanchu.churiservicesv2.customers.application.service.UpdateDelegationService
import org.bastanchu.churiservicesv2.customers.domain.Address
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegation
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegationRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.DelegationNotFoundException
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
class UpdateDelegationUseCaseTest {

    @Mock
    private lateinit var delegationRepository: CustomerDelegationRepository

    @InjectMocks
    private lateinit var service: UpdateDelegationService

    private val existing = CustomerDelegation(
        delegationId = 50L,
        customerId = 1L,
        orderId = 3,
        name = "Madrid Branch",
        address = Address(20L, "LOCAL", "ACME Madrid", "Branch St 1", "28010", "Madrid", "ES", "28")
    )

    private val command = UpdateDelegationCommand(
        orderId = null,
        name = "Madrid Branch Updated",
        address = AddressCommand("ACME Madrid", "Branch St 1", "28010", "Madrid", "ES", "28"),
        billingAddress = null
    )

    @Test
    fun `should keep existing orderId when not provided in command`() {
        whenever(delegationRepository.findById(50L)).thenReturn(existing)
        whenever(delegationRepository.save(any())).thenAnswer { it.arguments[0] as CustomerDelegation }

        val result = service.execute(50L, command)

        assertEquals(3, result.orderId)
        assertEquals("Madrid Branch Updated", result.name)
    }

    @Test
    fun `should accept the same orderId without collision check`() {
        whenever(delegationRepository.findById(50L)).thenReturn(existing)
        whenever(delegationRepository.save(any())).thenAnswer { it.arguments[0] as CustomerDelegation }

        val result = service.execute(50L, command.copy(orderId = 3))

        assertEquals(3, result.orderId)
    }

    @Test
    fun `should throw when provided orderId collides with another delegation of same customer`() {
        whenever(delegationRepository.findById(50L)).thenReturn(existing)
        whenever(delegationRepository.existsByCustomerIdAndOrderId(1L, 9, 50L)).thenReturn(true)

        assertThrows(InvalidDelegationException::class.java) {
            service.execute(50L, command.copy(orderId = 9))
        }
    }

    @Test
    fun `should throw when delegation does not exist`() {
        whenever(delegationRepository.findById(99L)).thenReturn(null)

        assertThrows(DelegationNotFoundException::class.java) {
            service.execute(99L, command)
        }
    }
}
