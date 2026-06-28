package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.common.domain.pagination.SortCriterion
import org.bastanchu.churiservicesv2.common.domain.pagination.SortDirection
import org.bastanchu.churiservicesv2.customers.application.dto.DelegationFilterDto
import org.bastanchu.churiservicesv2.customers.application.service.GetDelegationsByFilterService
import org.bastanchu.churiservicesv2.customers.domain.Address
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegation
import org.bastanchu.churiservicesv2.customers.domain.CustomerDelegationRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetDelegationsByFilterUseCaseTest {

    @Mock
    private lateinit var delegationRepository: CustomerDelegationRepository

    @InjectMocks
    private lateinit var service: GetDelegationsByFilterService

    @Test
    fun `should return a paged result and echo applied sort`() {
        val delegation = CustomerDelegation(
            delegationId = 50L,
            customerId = 1L,
            orderId = 0,
            name = "Madrid Branch",
            address = Address(20L, "LOCAL", "ACME Madrid", "Branch St 1", "28010", "Madrid", "ES", "28")
        )
        whenever(delegationRepository.findByFilter(eq(null), any())).thenReturn(
            FilteredPage(
                content = listOf(delegation),
                offset = 0L,
                limit = 20,
                hasMore = false,
                sort = listOf(
                    SortCriterion("name", SortDirection.ASC),
                    SortCriterion("delegationId", SortDirection.ASC)
                )
            )
        )

        val result = service.execute(DelegationFilterDto(), PageRequest.defaults())

        assertEquals(1, result.content.size)
        assertEquals("Madrid Branch", result.content[0].name)
        assertEquals(false, result.hasMore)
    }
}
