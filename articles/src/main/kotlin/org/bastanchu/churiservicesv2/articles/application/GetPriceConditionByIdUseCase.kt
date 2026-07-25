package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto

interface GetPriceConditionByIdUseCase {
    fun execute(conditionId: Long): PriceConditionDto
}
