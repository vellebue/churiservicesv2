package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionDto
import org.bastanchu.churiservicesv2.articles.application.dto.PriceConditionValueDto
import org.bastanchu.churiservicesv2.articles.domain.PriceCondition
import org.bastanchu.churiservicesv2.articles.domain.PriceConditionValue

fun PriceCondition.toDto(): PriceConditionDto = PriceConditionDto(
    conditionId = conditionId!!,
    conditionType = conditionType,
    description = description,
    scope = scope,
    evaluation = evaluation,
    availability = availability,
    numOrder = numOrder,
    values = values.sortedBy { it.beginDate }.map { it.toDto() }
)

fun PriceConditionValue.toDto(): PriceConditionValueDto = PriceConditionValueDto(
    valueId = valueId!!,
    type = type,
    value = value,
    beginDate = beginDate,
    endDate = endDate
)
