package org.bastanchu.churiservicesv2.articles.application.dto

import java.math.BigDecimal
import java.time.LocalDate

data class PriceConditionValueDto(
    val valueId: Long,
    val conditionId: Long,
    val valueType: Char,
    val value: BigDecimal,
    val beginDate: LocalDate,
    val endDate: LocalDate?
)
