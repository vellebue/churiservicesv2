package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.command.PriceConditionValueCommand
import org.bastanchu.churiservicesv2.articles.application.command.UpdatePriceConditionCommand
import org.bastanchu.churiservicesv2.articles.application.service.UpdatePriceConditionService
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionType
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionTypeRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionValue
import org.bastanchu.churiservicesv2.articles.domain.exception.InvalidPriceConditionException
import org.bastanchu.churiservicesv2.articles.domain.exception.PriceConditionNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class UpdatePriceConditionUseCaseTest {

    @Mock
    private lateinit var priceConditionRepository: PriceConditionRepository

    @Mock
    private lateinit var priceConditionTypeRepository: PriceConditionTypeRepository

    @InjectMocks
    private lateinit var useCase: UpdatePriceConditionService

    private val existingCondition = PriceCondition(
        conditionId = 1L,
        conditionType = "FARE",
        description = "Old description",
        scope = 'L',
        evaluation = 'D',
        availability = 'A',
        numOrder = 1,
        values = listOf(
            PriceConditionValue(
                valueId = 1L,
                type = 'A',
                value = BigDecimal("5.00"),
                beginDate = LocalDate.of(2026, 1, 1),
                endDate = LocalDate.of(2026, 6, 30)
            )
        )
    )

    private val validValueCommand = PriceConditionValueCommand(
        type = 'P',
        value = BigDecimal("15.00"),
        beginDate = LocalDate.of(2026, 1, 1),
        endDate = LocalDate.of(2026, 12, 31)
    )

    private val validCommand = UpdatePriceConditionCommand(
        conditionType = "DISC",
        description = "Updated discount condition",
        scope = 'H',
        evaluation = 'J',
        availability = 'A',
        numOrder = 2,
        values = listOf(validValueCommand)
    )

    private val discType = PriceConditionType(
        conditionType = "DISC",
        description = "Discount",
        key = "priceCondition.type.discount"
    )

    @Test
    fun `should update price condition with valid data`() {
        val savedCondition = PriceCondition(
            conditionId = 1L,
            conditionType = "DISC",
            description = "Updated discount condition",
            scope = 'H',
            evaluation = 'J',
            availability = 'A',
            numOrder = 2,
            values = listOf(
                PriceConditionValue(
                    valueId = 2L,
                    type = 'P',
                    value = BigDecimal("15.00"),
                    beginDate = LocalDate.of(2026, 1, 1),
                    endDate = LocalDate.of(2026, 12, 31)
                )
            )
        )

        whenever(priceConditionRepository.findById(1L)).thenReturn(existingCondition)
        whenever(priceConditionTypeRepository.findByConditionType("DISC")).thenReturn(discType)
        whenever(priceConditionRepository.save(any())).thenReturn(savedCondition)

        val result = useCase.execute(1L, validCommand)

        assertEquals(1L, result.conditionId)
        assertEquals("DISC", result.conditionType)
        assertEquals("Updated discount condition", result.description)
        assertEquals('H', result.scope)
        assertEquals('J', result.evaluation)
        assertEquals(2, result.numOrder)
        assertEquals(1, result.values.size)
    }

    @Test
    fun `should throw when price condition does not exist`() {
        whenever(priceConditionRepository.findById(99L)).thenReturn(null)

        assertThrows(PriceConditionNotFoundException::class.java) {
            useCase.execute(99L, validCommand)
        }
    }

    @Test
    fun `should throw when condition type does not exist`() {
        whenever(priceConditionRepository.findById(1L)).thenReturn(existingCondition)
        whenever(priceConditionTypeRepository.findByConditionType("DISC")).thenReturn(null)

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(1L, validCommand)
        }
    }

    @Test
    fun `should throw when scope is invalid`() {
        whenever(priceConditionRepository.findById(1L)).thenReturn(existingCondition)
        whenever(priceConditionTypeRepository.findByConditionType("DISC")).thenReturn(discType)

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(1L, validCommand.copy(scope = 'Z'))
        }
    }
}
