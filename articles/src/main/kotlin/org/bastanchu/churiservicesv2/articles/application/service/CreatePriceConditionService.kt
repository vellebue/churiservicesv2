package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.CreatePriceConditionUseCase
import org.bastanchu.churiservicesv2.articles.application.command.CreatePriceConditionCommand
import org.bastanchu.churiservicesv2.articles.application.command.PriceConditionValueCommand
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionTypeRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionValue
import org.bastanchu.churiservicesv2.articles.domain.exception.InvalidPriceConditionException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreatePriceConditionService(
    private val priceConditionRepository: PriceConditionRepository,
    private val priceConditionTypeRepository: PriceConditionTypeRepository
) : CreatePriceConditionUseCase {

    override fun execute(command: CreatePriceConditionCommand): PriceConditionDto {
        validateConditionType(command.conditionType)
        validateScope(command.scope)
        validateEvaluation(command.evaluation)
        validateAvailability(command.availability)
        validateValues(command.values)

        val priceCondition = PriceCondition(
            conditionType = command.conditionType,
            description = command.description,
            scope = command.scope,
            evaluation = command.evaluation,
            availability = command.availability,
            numOrder = command.numOrder,
            values = command.values.map { v ->
                PriceConditionValue(
                    type = v.type,
                    value = v.value,
                    beginDate = v.beginDate,
                    endDate = v.endDate
                )
            }
        )

        val saved = priceConditionRepository.save(priceCondition)
        return saved.toDto()
    }

    private fun validateConditionType(conditionType: String) {
        if (priceConditionTypeRepository.findByConditionType(conditionType) == null) {
            throw InvalidPriceConditionException("Price condition type '$conditionType' does not exist")
        }
    }

    private fun validateScope(scope: Char) {
        if (scope !in listOf('L', 'H', 'D')) {
            throw InvalidPriceConditionException("Invalid scope '$scope'. Must be 'L', 'H' or 'D'")
        }
    }

    private fun validateEvaluation(evaluation: Char) {
        if (evaluation !in listOf('D', 'J')) {
            throw InvalidPriceConditionException("Invalid evaluation '$evaluation'. Must be 'D' or 'J'")
        }
    }

    private fun validateAvailability(availability: Char) {
        if (availability !in listOf('A', 'S')) {
            throw InvalidPriceConditionException("Invalid availability '$availability'. Must be 'A' or 'S'")
        }
    }

    private fun validateValues(values: List<PriceConditionValueCommand>) {
        values.forEach { v ->
            if (v.type !in listOf('A', 'P')) {
                throw InvalidPriceConditionException("Invalid value type '${v.type}'. Must be 'A' or 'P'")
            }
            if (v.endDate != null && v.endDate.isBefore(v.beginDate)) {
                throw InvalidPriceConditionException("End date must be on or after begin date")
            }
        }
        validateNoOverlappingDates(values)
    }

    private fun validateNoOverlappingDates(values: List<PriceConditionValueCommand>) {
        val nullEndDateCount = values.count { it.endDate == null }
        if (nullEndDateCount > 1) {
            throw InvalidPriceConditionException("Only one price condition value can have a null end date")
        }

        val sorted = values.sortedBy { it.beginDate }
        for (i in 0 until sorted.size - 1) {
            val current = sorted[i]
            val next = sorted[i + 1]
            val currentEnd = current.endDate
            if (currentEnd == null || !currentEnd.isBefore(next.beginDate)) {
                throw InvalidPriceConditionException("Price condition value date intervals must not overlap")
            }
        }
    }
}
