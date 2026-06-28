package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.customers.application.service.GetDelegationByIdService
import org.bastanchu.churiservicesv2.customers.domain.Address
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegation
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegationRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.DelegationNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetDelegationByIdUseCaseTest {

    @Mock
    private lateinit var delegationRepository: CustomerDelegationRepository

    @InjectMocks
    private lateinit var service: GetDelegationByIdService

    @Test
    fun `should return delegation when found`() {
        val delegation = CustomerDelegation(
            delegationId = 50L,
            customerId = 1L,
            orderId = 0,
            name = "Madrid Branch",
            address = Address(20L, "LOCAL", "ACME Madrid", "Branch St 1", "28010", "Madrid", "ES", "28")
        )
        whenever(delegationRepository.findById(50L)).thenReturn(delegation)

        val result = service.execute(50L)

        assertEquals(50L, result.delegationId)
        assertEquals("Madrid Branch", result.name)
    }

    @Test
    fun `should throw when delegation does not exist`() {
        whenever(delegationRepository.findById(99L)).thenReturn(null)

        assertThrows(DelegationNotFoundException::class.java) {
            service.execute(99L)
        }
    }
}
