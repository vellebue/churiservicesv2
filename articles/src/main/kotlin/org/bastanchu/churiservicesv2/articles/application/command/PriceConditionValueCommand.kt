package org.bastanchu.churiservicesv2.articles.application.command

import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class PriceConditionValueCommand(
    @field:NotNull
    val valueType: Char,

    @field:NotNull
    val value: BigDecimal,

    @field:NotNull
    val beginDate: LocalDate,

    val endDate: LocalDate? = null
)
