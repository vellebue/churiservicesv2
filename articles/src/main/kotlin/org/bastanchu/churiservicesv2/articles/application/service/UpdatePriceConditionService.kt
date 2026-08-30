package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.UpdatePriceConditionUseCase
import org.bastanchu.churiservicesv2.articles.application.command.PriceConditionValueCommand
import org.bastanchu.churiservicesv2.articles.application.command.UpdatePriceConditionCommand
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionSubtypeRepository
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionValue
import org.bastanchu.churiservicesv2.articles.domain.exception.InvalidPriceConditionException
import org.bastanchu.churiservicesv2.articles.domain.exception.PriceConditionNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UpdatePriceConditionService(
    private val priceConditionRepository: PriceConditionRepository,
    private val priceConditionSubtypeRepository: PriceConditionSubtypeRepository
) : UpdatePriceConditionUseCase {

    override fun execute(conditionId: Long, command: UpdatePriceConditionCommand): PriceConditionDto {
        priceConditionRepository.findById(conditionId)
            ?: throw PriceConditionNotFoundException(conditionId)

        if (priceConditionSubtypeRepository.findById(command.subtypeId) == null) {
            throw InvalidPriceConditionException("Price condition subtype ${command.subtypeId} does not exist")
        }
        PriceConditionValidator.validateValues(command.values)

        val replacement = PriceCondition(
            conditionId = conditionId,
            subtypeId = command.subtypeId,
            scope = command.scope,
            evaluation = command.evaluation,
            description = command.description,
            values = command.values.map { it.toDomain() }
        )
        return priceConditionRepository.save(replacement).toDto()
    }

    private fun PriceConditionValueCommand.toDomain(): PriceConditionValue = PriceConditionValue(
        conditionId = 0,
        valueType = valueType,
        value = value,
        beginDate = beginDate,
        endDate = endDate
    )
}
