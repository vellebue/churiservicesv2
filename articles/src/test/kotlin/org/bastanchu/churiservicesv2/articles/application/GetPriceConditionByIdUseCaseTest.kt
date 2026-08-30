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
    fun `should return price condition with its values`() {
        val priceCondition = PriceCondition(
            conditionId = 7L,
            subtypeId = 10L,
            scope = 'H',
            evaluation = 'D',
            description = "d",
            values = listOf(
                PriceConditionValue(
                    valueId = 1L,
                    conditionId = 7L,
                    valueType = 'A',
                    value = BigDecimal("1.0"),
                    beginDate = LocalDate.of(2026, 1, 1),
                    endDate = null
                )
            )
        )
        whenever(priceConditionRepository.findById(7L)).thenReturn(priceCondition)

        val result = useCase.execute(7L)

        assertEquals(7L, result.conditionId)
        assertEquals(1, result.values.size)
    }

    @Test
    fun `should throw when price condition does not exist`() {
        whenever(priceConditionRepository.findById(99L)).thenReturn(null)

        assertThrows(PriceConditionNotFoundException::class.java) {
            useCase.execute(99L)
        }
    }
}
