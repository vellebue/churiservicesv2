package org.bastanchu.churiservicesv2.articles.application.dto

data class PriceConditionDto(
    val conditionId: Long,
    val subtypeId: Long,
    val scope: Char,
    val evaluation: Char,
    val description: String,
    val values: List<PriceConditionValueDto>
)
