package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionValueDto
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionValue

fun PriceCondition.toDto(): PriceConditionDto = PriceConditionDto(
    conditionId = conditionId!!,
    subtypeId = subtypeId,
    scope = scope,
    evaluation = evaluation,
    description = description,
    values = values.map { it.toDto(conditionId) }
)

fun PriceConditionValue.toDto(conditionId: Long): PriceConditionValueDto = PriceConditionValueDto(
    valueId = valueId!!,
    conditionId = conditionId,
    valueType = valueType,
    value = value,
    beginDate = beginDate,
    endDate = endDate
)
