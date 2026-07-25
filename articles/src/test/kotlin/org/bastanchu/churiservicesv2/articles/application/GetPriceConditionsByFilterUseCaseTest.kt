package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionFilterDto
import org.bastanchu.churiservicesv2.articles.application.service.GetPriceConditionsByFilterService
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionValue
import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class GetPriceConditionsByFilterUseCaseTest {

    @Mock
    private lateinit var priceConditionRepository: PriceConditionRepository

    @InjectMocks
    private lateinit var useCase: GetPriceConditionsByFilterService

    @Test
    fun `should return filtered page of price conditions`() {
        val conditions = listOf(
            PriceCondition(
                conditionId = 1L,
                conditionType = "FARE",
                description = "Base price",
                scope = 'L',
                evaluation = 'D',
                availability = 'A',
                numOrder = 1,
                values = listOf(
                    PriceConditionValue(
                        valueId = 1L,
                        type = 'A',
                        value = BigDecimal("10.00"),
                        beginDate = LocalDate.of(2026, 1, 1)
                    )
                )
            )
        )

        val pageRequest = PageRequest.defaults()
        val filteredPage = FilteredPage(
            content = conditions,
            offset = 0L,
            limit = 20,
            hasMore = false,
            sort = emptyList()
        )

        whenever(priceConditionRepository.findByFilter(eq("FARE"), eq(null), any()))
            .thenReturn(filteredPage)

        val filter = PriceConditionFilterDto(conditionType = "FARE")
        val result = useCase.execute(filter, pageRequest)

        assertEquals(1, result.content.size)
        assertEquals("FARE", result.content[0].conditionType)
        assertEquals(false, result.hasMore)
    }

    @Test
    fun `should convert wildcard to SQL LIKE pattern`() {
        val pageRequest = PageRequest.defaults()
        val filteredPage = FilteredPage<PriceCondition>(
            content = emptyList(),
            offset = 0L,
            limit = 20,
            hasMore = false,
            sort = emptyList()
        )

        whenever(priceConditionRepository.findByFilter(eq(null), eq("%discount%"), any()))
            .thenReturn(filteredPage)

        val filter = PriceConditionFilterDto(description = "*discount*")
        val result = useCase.execute(filter, pageRequest)

        assertEquals(0, result.content.size)
    }
}
