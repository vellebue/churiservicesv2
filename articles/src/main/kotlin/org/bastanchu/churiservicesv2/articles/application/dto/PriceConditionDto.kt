package org.bastanchu.churiservicesv2.articles.application.dto

data class PriceConditionDto(
    val conditionId: Long,
    val conditionType: String,
    val description: String,
    val scope: Char,
    val evaluation: Char,
    val availability: Char,
    val numOrder: Int,
    val values: List<PriceConditionValueDto>
)
