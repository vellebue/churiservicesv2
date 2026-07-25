package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.GetPriceConditionByIdUseCase
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.exception.PriceConditionNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetPriceConditionByIdService(
    private val priceConditionRepository: PriceConditionRepository
) : GetPriceConditionByIdUseCase {

    override fun execute(conditionId: Long): PriceConditionDto {
        val priceCondition = priceConditionRepository.findById(conditionId)
            ?: throw PriceConditionNotFoundException(conditionId)
        return priceCondition.toDto()
    }
}
