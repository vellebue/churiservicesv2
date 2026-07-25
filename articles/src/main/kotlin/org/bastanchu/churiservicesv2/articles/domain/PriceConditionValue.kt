package org.bastanchu.churiservicesv2.articles.domain

import java.math.BigDecimal
import java.time.LocalDate

data class PriceConditionValue(
    val valueId: Long? = null,
    val type: Char,
    val value: BigDecimal,
    val beginDate: LocalDate,
    val endDate: LocalDate? = null
)
