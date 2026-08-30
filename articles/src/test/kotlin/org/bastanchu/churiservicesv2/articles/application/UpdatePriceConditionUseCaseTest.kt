package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.command.PriceConditionValueCommand
import org.bastanchu.churiservicesv2.articles.application.command.UpdatePriceConditionCommand
import org.bastanchu.churiservicesv2.articles.application.service.UpdatePriceConditionService
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionSubtype
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionSubtypeRepository
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
    private lateinit var subtypeRepository: PriceConditionSubtypeRepository

    @InjectMocks
    private lateinit var useCase: UpdatePriceConditionService

    private val existing = PriceCondition(
        conditionId = 5L,
        subtypeId = 10L,
        scope = 'L',
        evaluation = 'D',
        description = "old description",
        values = emptyList()
    )

    private val subtype = PriceConditionSubtype(
        subtypeId = 10L,
        conditionType = "DISC",
        conditionSubtype = "DISS",
        description = "Standard discount",
        key = "priceCondition.subtype.disc.diss"
    )

    private val validValue = PriceConditionValueCommand(
        valueType = 'A',
        value = BigDecimal("5.0"),
        beginDate = LocalDate.of(2026, 1, 1),
        endDate = null
    )

    private val validCommand = UpdatePriceConditionCommand(
        subtypeId = 10L,
        scope = 'L',
        evaluation = 'J',
        description = "new description",
        values = listOf(validValue)
    )

    @Test
    fun `should update price condition replacing values`() {
        whenever(priceConditionRepository.findById(5L)).thenReturn(existing)
        whenever(subtypeRepository.findById(10L)).thenReturn(subtype)
        whenever(priceConditionRepository.save(any())).thenReturn(
            existing.copy(
                evaluation = 'J',
                description = "new description",
                values = listOf(
                    PriceConditionValue(
                        valueId = 1L,
                        conditionId = 5L,
                        valueType = 'A',
                        value = BigDecimal("5.0"),
                        beginDate = LocalDate.of(2026, 1, 1),
                        endDate = null
                    )
                )
            )
        )

        val result = useCase.execute(5L, validCommand)

        assertEquals(5L, result.conditionId)
        assertEquals('J', result.evaluation)
        assertEquals("new description", result.description)
        assertEquals(1, result.values.size)
    }

    @Test
    fun `should throw when price condition does not exist`() {
        whenever(priceConditionRepository.findById(5L)).thenReturn(null)

        assertThrows(PriceConditionNotFoundException::class.java) {
            useCase.execute(5L, validCommand)
        }
    }

    @Test
    fun `should throw when subtype does not exist`() {
        whenever(priceConditionRepository.findById(5L)).thenReturn(existing)
        whenever(subtypeRepository.findById(10L)).thenReturn(null)

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(5L, validCommand)
        }
    }

    @Test
    fun `should throw when values overlap`() {
        whenever(priceConditionRepository.findById(5L)).thenReturn(existing)
        whenever(subtypeRepository.findById(10L)).thenReturn(subtype)
        val a = validValue.copy(
            beginDate = LocalDate.of(2026, 1, 1),
            endDate = LocalDate.of(2026, 6, 30)
        )
        val b = validValue.copy(
            beginDate = LocalDate.of(2026, 5, 1),
            endDate = null
        )

        assertThrows(InvalidPriceConditionException::class.java) {
            useCase.execute(5L, validCommand.copy(values = listOf(a, b)))
        }
    }
}
