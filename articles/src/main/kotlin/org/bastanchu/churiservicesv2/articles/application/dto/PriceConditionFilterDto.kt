package org.bastanchu.churiservicesv2.articles.application.dto

import java.time.LocalDate

data class PriceConditionFilterDto(
    val subtypeId: Long? = null,
    val scope: Char? = null,
    val evaluation: Char? = null,
    val description: String? = null,
    val beginDate: LocalDate? = null,
    val endDate: LocalDate? = null
)
