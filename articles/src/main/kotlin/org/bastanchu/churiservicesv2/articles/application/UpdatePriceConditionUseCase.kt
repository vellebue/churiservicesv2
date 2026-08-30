package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.command.UpdatePriceConditionCommand
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto

interface UpdatePriceConditionUseCase {
    fun execute(conditionId: Long, command: UpdatePriceConditionCommand): PriceConditionDto
}
