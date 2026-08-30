package org.bastanchu.churiservicesv2.articles.application

import org.bastanchu.churiservicesv2.articles.application.command.CreatePriceConditionCommand
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto

interface CreatePriceConditionUseCase {
    fun execute(command: CreatePriceConditionCommand): PriceConditionDto
}
