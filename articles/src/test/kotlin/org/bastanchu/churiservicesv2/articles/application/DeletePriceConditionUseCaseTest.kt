package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.service.DeletePriceConditionService
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionValue
import org.bastanchu.churiservicesv2.articles.domain.exception.PriceConditionNotFoundException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class DeletePriceConditionUseCaseTest {

    @Mock
    private lateinit var priceConditionRepository: PriceConditionRepository

    @InjectMocks
    private lateinit var useCase: DeletePriceConditionService

    private val existingCondition = PriceCondition(
        conditionId = 1L,
        conditionType = "FARE",
        description = "Test condition",
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

    @Test
    fun `should delete price condition when it exists`() {
        whenever(priceConditionRepository.findById(1L)).thenReturn(existingCondition)

        useCase.execute(1L)

        verify(priceConditionRepository).deleteById(1L)
    }

    @Test
    fun `should throw when price condition does not exist`() {
        whenever(priceConditionRepository.findById(99L)).thenReturn(null)

        assertThrows(PriceConditionNotFoundException::class.java) {
            useCase.execute(99L)
        }
    }
}
