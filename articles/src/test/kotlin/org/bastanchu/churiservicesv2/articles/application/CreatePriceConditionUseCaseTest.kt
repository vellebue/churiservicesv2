package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.command.CreatePriceConditionCommand
import org.bastanchu.churiservicesv2.articles.application.command.PriceConditionValueCommand
import org.bastanchu.churiservicesv2.articles.application.service.CreatePriceConditionService
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionSubtype
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionSubtypeRepository
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
    private lateinit var subtypeRepository: PriceConditionSubtypeRepository

    @InjectMocks
    private lateinit var useCase: CreatePriceConditionService

    private val subtype = PriceConditionSubtype(
        subtypeId = 10L,
        conditionType = "DISC",
        conditionSubtype = "DISS",
        description = "Standard discount",
        key = "priceCondition.subtype.disc.diss"
    )

    private val openValue = PriceConditionValueCommand(
        valueType = 'P',
        value = BigDecimal("10.5"),
        beginDate = LocalDate.of(2026, 1, 1),
        endDate = null
    )

    private val validCommand = CreatePriceConditionCommand(
        subtypeId = 10L,
        scope = 'L',
        evaluation = 'D',
        description = "Standard 10.5% discount",
        values = listOf(openValue)
    )

    @Test
    fun `should create price condition with valid open ended value`() {
        whenever(subtypeRepository.findById(10L)).thenReturn(subtype)
        whenever(priceConditionRepository.save(any())).thenReturn(
            PriceCondition(
                conditionId = 1L,
                subtypeId = 10L,
                scope = 'L',
                evaluation = 'D',
                description = "Standard 10.5% discount",
                values = listOf(
                    PriceConditionValue(
                        valueId = 1L,
                        conditionId = 1L,
                        valueType = 'P',
                        value = BigDecimal("10.5"),
                        beginDate = LocalDate.of(2026, 1, 1),
                        endDate = null
                    )
                )
            )
        )

        val result = useCase.execute(validCommand)

        assertEquals(1L, result.conditionId)
        assertEquals(10L, result.subtypeId)
        assertEquals('L', result.scope)
        assertEquals("Standard 10.5% discount", result.description)
        assertEquals(1, result.values.size)
        assertEquals(null, result.values[0].endDate)
    }

    @Test
    fun `should throw when subtype does not exist`() {
        whenever(subtypeRepository.findById(10L)).thenReturn(null)

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand)
        }
    }

    @Test
    fun `should throw when no open ended value is provided`() {
        whenever(subtypeRepository.findById(10L)).thenReturn(subtype)
        val closed = openValue.copy(endDate = LocalDate.of(2026, 12, 31))

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(values = listOf(closed)))
        }
    }

    @Test
    fun `should throw when values overlap`() {
        whenever(subtypeRepository.findById(10L)).thenReturn(subtype)
        val a = openValue.copy(
            beginDate = LocalDate.of(2026, 1, 1),
            endDate = LocalDate.of(2026, 6, 30)
        )
        val b = openValue.copy(
            beginDate = LocalDate.of(2026, 6, 30),
            endDate = null
        )

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(values = listOf(a, b)))
        }
    }

    @Test
    fun `should throw when a value has endDate before beginDate`() {
        whenever(subtypeRepository.findById(10L)).thenReturn(subtype)
        val bogus = openValue.copy(
            beginDate = LocalDate.of(2026, 6, 1),
            endDate = LocalDate.of(2026, 1, 1)
        )

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(validCommand.copy(values = listOf(bogus)))
        }
    }
}
