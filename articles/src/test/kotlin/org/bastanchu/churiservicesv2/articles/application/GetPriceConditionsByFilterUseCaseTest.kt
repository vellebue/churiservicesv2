package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionFilterDto
import org.bastanchu.churiservicesv2.articles.application.service.GetPriceConditionsByFilterService
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionFilter
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.bastanchu.churiservicesv2.common.domain.pagination.SortCriterion
import org.bastanchu.churiservicesv2.common.domain.pagination.SortDirection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetPriceConditionsByFilterUseCaseTest {

    @Mock
    private lateinit var priceConditionRepository: PriceConditionRepository

    @InjectMocks
    private lateinit var useCase: GetPriceConditionsByFilterService

    private val samplePage = FilteredPage(
        content = listOf(
            PriceCondition(
                conditionId = 1L,
                subtypeId = 10L,
                scope = 'L',
                evaluation = 'D',
                description = "d"
            )
        ),
        offset = 0L,
        limit = 20,
        hasMore = false,
        sort = listOf(SortCriterion("conditionId", SortDirection.ASC))
    )

    @Test
    fun `should map domain filter and delegate to repository`() {
        val filterDto = PriceConditionFilterDto(
            subtypeId = 10L,
            scope = 'L',
            description = "std*"
        )
        val filterCaptor = argumentCaptor<PriceConditionFilter>()
        whenever(priceConditionRepository.findByFilter(filterCaptor.capture(), any()))
            .thenReturn(samplePage)

        val result = useCase.execute(filterDto, PageRequest.defaults())

        assertEquals(1, result.content.size)
        assertEquals(10L, filterCaptor.firstValue.subtypeId)
        assertEquals('L', filterCaptor.firstValue.scope)
        assertEquals("std*", filterCaptor.firstValue.descriptionPattern)
    }

    @Test
    fun `should append conditionId tie-breaker to sort`() {
        whenever(priceConditionRepository.findByFilter(any(), any())).thenReturn(samplePage)
        val requested = PageRequest(
            offset = 0L,
            limit = 20,
            sort = listOf(SortCriterion("description", SortDirection.DESC))
        )

        val result = useCase.execute(PriceConditionFilterDto(), requested)

        // Response echoes what came from the repository — but sort should always end with conditionId asc.
        // Verify the repository was called with a resolved page request that ends with the tie-breaker.
        val pageCaptor = argumentCaptor<PageRequest>()
        whenever(priceConditionRepository.findByFilter(eq(PriceConditionFilter()), pageCaptor.capture()))
            .thenReturn(samplePage)
        useCase.execute(PriceConditionFilterDto(), requested)
        val resolvedSort = pageCaptor.firstValue.sort
        assertTrue(resolvedSort.last().field == "conditionId" && resolvedSort.last().direction == SortDirection.ASC)
        assertEquals(1, result.content.size)
    }
}
