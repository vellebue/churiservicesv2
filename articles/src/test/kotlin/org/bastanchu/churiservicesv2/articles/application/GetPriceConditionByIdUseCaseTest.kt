package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.service.GetPriceConditionByIdService
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionValue
import org.bastanchu.churiservicesv2.articles.domain.exception.PriceConditionNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class GetPriceConditionByIdUseCaseTest {

    @Mock
    private lateinit var priceConditionRepository: PriceConditionRepository

    @InjectMocks
    private lateinit var useCase: GetPriceConditionByIdService

    @Test
    fun `should return price condition when found`() {
        val condition = PriceCondition(
            conditionId = 1L,
            conditionType = "TAXE",
            description = "Standard VAT",
            scope = 'L',
            evaluation = 'D',
            availability = 'A',
            numOrder = 3,
            values = listOf(
                PriceConditionValue(
                    valueId = 1L,
                    type = 'P',
                    value = BigDecimal("21.000000"),
                    beginDate = LocalDate.of(2026, 1, 1),
                    endDate = null
                )
            )
        )

        whenever(priceConditionRepository.findById(1L)).thenReturn(condition)

        val result = useCase.execute(1L)

        assertEquals(1L, result.conditionId)
        assertEquals("TAXE", result.conditionType)
        assertEquals("Standard VAT", result.description)
        assertEquals(1, result.values.size)
        assertEquals('P', result.values[0].type)
    }

    @Test
    fun `should throw when price condition not found`() {
        whenever(priceConditionRepository.findById(99L)).thenReturn(null)

        assertThrows(PriceConditionNotFoundException::class.java) {
            useCase.execute(99L)
        }
    }
}
