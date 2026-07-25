package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.command.CreatePriceConditionCommand
import org.bastanchu.churiservicesv2.articles.application.command.PriceConditionValueCommand
import org.bastanchu.churiservicesv2.articles.application.service.CreatePriceConditionService
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionType
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionTypeRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionValue
import org.bastanchu.churiservicesv2.articles.domain.exception.InvalidPriceConditionException
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
class CreatePriceConditionUseCaseTest {

    @Mock
    private lateinit var priceConditionRepository: PriceConditionRepository

    @Mock
    private lateinit var priceConditionTypeRepository: PriceConditionTypeRepository

    @InjectMocks
    private lateinit var useCase: CreatePriceConditionService

    private val validValueCommand = PriceConditionValueCommand(
        type = 'A',
        value = BigDecimal("10.50"),
        beginDate = LocalDate.of(2026, 1, 1),
        endDate = LocalDate.of(2026, 6, 30)
    )

    private val validCommand = CreatePriceConditionCommand(
        conditionType = "FARE",
        description = "Base price for standard articles",
        scope = 'L',
        evaluation = 'D',
        availability = 'A',
        numOrder = 1,
        values = listOf(validValueCommand)
    )

    private val fareType = PriceConditionType(
        conditionType = "FARE",
        description = "Base Article Price",
        key = "priceCondition.type.fare"
    )

    @Test
    fun `should create price condition with valid data`() {
        val savedCondition = PriceCondition(
            conditionId = 1L,
            conditionType = "FARE",
            description = "Base price for standard articles",
            scope = 'L',
            evaluation = 'D',
            availability = 'A',
            numOrder = 1,
            values = listOf(
                PriceConditionValue(
                    valueId = 1L,
                    type = 'A',
                    value = BigDecimal("10.50"),
                    beginDate = LocalDate.of(2026, 1, 1),
                    endDate = LocalDate.of(2026, 6, 30)
                )
            )
        )

        whenever(priceConditionTypeRepository.findByConditionType("FARE")).thenReturn(fareType)
        whenever(priceConditionRepository.save(any())).thenReturn(savedCondition)

        val result = useCase.execute(validCommand)

        assertEquals(1L, result.conditionId)
        assertEquals("FARE", result.conditionType)
        assertEquals("Base price for standard articles", result.description)
        assertEquals('L', result.scope)
        assertEquals('D', result.evaluation)
        assertEquals('A', result.availability)
        assertEquals(1, result.numOrder)
        assertEquals(1, result.values.size)
        assertEquals(BigDecimal("10.50"), result.values[0].value)
    }

    @Test
    fun `should throw when condition type does not exist`() {
        whenever(priceConditionTypeRepository.findByConditionType("FAKE")).thenReturn(null)

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(conditionType = "FAKE"))
        }
    }

    @Test
    fun `should throw when scope is invalid`() {
        whenever(priceConditionTypeRepository.findByConditionType("FARE")).thenReturn(fareType)

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(scope = 'X'))
        }
    }

    @Test
    fun `should throw when evaluation is invalid`() {
        whenever(priceConditionTypeRepository.findByConditionType("FARE")).thenReturn(fareType)

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(evaluation = 'X'))
        }
    }

    @Test
    fun `should throw when availability is invalid`() {
        whenever(priceConditionTypeRepository.findByConditionType("FARE")).thenReturn(fareType)

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(availability = 'X'))
        }
    }

    @Test
    fun `should throw when value type is invalid`() {
        whenever(priceConditionTypeRepository.findByConditionType("FARE")).thenReturn(fareType)

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(values = listOf(validValueCommand.copy(type = 'X'))))
        }
    }

    @Test
    fun `should throw when end date is before begin date`() {
        whenever(priceConditionTypeRepository.findByConditionType("FARE")).thenReturn(fareType)

        val badValue = validValueCommand.copy(
            beginDate = LocalDate.of(2026, 6, 1),
            endDate = LocalDate.of(2026, 1, 1)
        )

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(values = listOf(badValue)))
        }
    }

    @Test
    fun `should throw when multiple values have null end date`() {
        whenever(priceConditionTypeRepository.findByConditionType("FARE")).thenReturn(fareType)

        val values = listOf(
            validValueCommand.copy(beginDate = LocalDate.of(2026, 1, 1), endDate = null),
            validValueCommand.copy(beginDate = LocalDate.of(2026, 7, 1), endDate = null)
        )

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(values = values))
        }
    }

    @Test
    fun `should throw when value date intervals overlap`() {
        whenever(priceConditionTypeRepository.findByConditionType("FARE")).thenReturn(fareType)

        val values = listOf(
            validValueCommand.copy(beginDate = LocalDate.of(2026, 1, 1), endDate = LocalDate.of(2026, 6, 30)),
            validValueCommand.copy(beginDate = LocalDate.of(2026, 6, 15), endDate = LocalDate.of(2026, 12, 31))
        )

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(values = values))
        }
    }

    @Test
    fun `should throw InvalidPriceConditionException when a value date interval is included into another one`() {
        whenever(priceConditionTypeRepository.findByConditionType("FARE")).thenReturn(fareType)

        val values = listOf(
            validValueCommand.copy(beginDate = LocalDate.of(2026, 1, 1), endDate = LocalDate.of(2026, 6, 30)),
            validValueCommand.copy(beginDate = LocalDate.of(2026, 2, 1), endDate = LocalDate.of(2026, 4, 30))
        )

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(values = values))
        }
    }
}
