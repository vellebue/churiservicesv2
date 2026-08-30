package org.bastanchu.churiservicesv2.articles.domain

import java.time.LocalDate

data class PriceConditionFilter(
    val subtypeId: Long? = null,
    val scope: Char? = null,
    val evaluation: Char? = null,
    val descriptionPattern: String? = null,
    val beginDate: LocalDate? = null,
    val endDate: LocalDate? = null
)
