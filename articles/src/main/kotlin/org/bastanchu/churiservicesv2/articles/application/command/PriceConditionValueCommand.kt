package org.bastanchu.churiservicesv2.articles.application.command

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate

data class PriceConditionValueCommand(
    @field:NotNull
    val type: Char,

    @field:NotNull
    val value: BigDecimal,

    @field:NotNull
    val beginDate: LocalDate,

    val endDate: LocalDate? = null
)
