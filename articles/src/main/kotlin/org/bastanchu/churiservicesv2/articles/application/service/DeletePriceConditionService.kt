package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.DeletePriceConditionUseCase
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionRepository
import org.bastanchu.churiservicesv2.articles.domain.exception.PriceConditionNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeletePriceConditionService(
    private val priceConditionRepository: PriceConditionRepository
) : DeletePriceConditionUseCase {

    override fun execute(conditionId: Long) {
        priceConditionRepository.findById(conditionId)
            ?: throw PriceConditionNotFoundException(conditionId)
        priceConditionRepository.deleteById(conditionId)
    }
}
