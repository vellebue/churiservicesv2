package org.bastanchu.churiservicesv2.articles.application.dto

import java.math.BigDecimal
import java.time.LocalDate

data class PriceConditionValueDto(
    val valueId: Long,
    val type: Char,
    val value: BigDecimal,
    val beginDate: LocalDate,
    val endDate: LocalDate? = null
)
