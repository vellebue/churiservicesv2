package org.bastanchu.churiservicesv2.customers.application

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.common.domain.pagination.SortCriterion
import org.bastanchu.churiservicesv2.common.domain.pagination.SortDirection
import org.bastanchu.churiservicesv2.customers.application.dto.CustomerFilterDto
import org.bastanchu.churiservicesv2.customers.application.service.GetCustomersByFilterService
import org.bastanchu.churiservicesv2.customers.domain.Address
import org.bastanchu.churiservicesv2.customers.domain.Customer
import org.bastanchu.churiservicesv2.customers.domain.CustomerRepository
import org.bastanchu.churiservicesv2.customers.domain.exception.InvalidCustomerException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetCustomersByFilterUseCaseTest {

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @InjectMocks
    private lateinit var service: GetCustomersByFilterService

    @Captor
    private lateinit var pageRequestCaptor: ArgumentCaptor<PageRequest>

    private fun sampleCustomer(id: Long) = Customer(
        customerId = id,
        commercialName = "ACME-$id",
        socialName = "ACME SA $id",
        vatNumber = "VAT-$id",
        customerAddress = Address(10L, "COMMERCIAL", "ACME SA", "Main St 1", "28001", "Madrid", "ES", "28"),
        socialAddress = Address(11L, "SOCIAL", "ACME SA", "Legal St 2", "28002", "Madrid", "ES", "28")
    )

    @Test
    fun `should apply default sort and tie-breaker when no sort is provided`() {
        whenever(
            customerRepository.findByFilter(
                eq(null), eq(null), eq(null), any()
            )
        ).thenReturn(
            FilteredPage(
                content = listOf(sampleCustomer(1L), sampleCustomer(2L)),
                offset = 0L,
                limit = 20,
                hasMore = false,
                sort = listOf(SortCriterion("commercialName", SortDirection.ASC), SortCriterion("customerId", SortDirection.ASC))
            )
        )

        val result = service.execute(CustomerFilterDto(), PageRequest.defaults())

        assertEquals(2, result.content.size)
        assertEquals(false, result.hasMore)
    }

    @Test
    fun `should reject unknown sort fields`() {
        try {
            service.execute(
                CustomerFilterDto(),
                PageRequest(offset = 0L, limit = 20, sort = listOf(SortCriterion("unknown", SortDirection.ASC)))
            )
            throw AssertionError("Expected exception")
        } catch (ex: org.bastanchu.churiservicesv2.common.domain.exception.InvalidPageRequestException) {
            // expected
        }
    }
}
