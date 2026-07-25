package org.bastanchu.churiservicesv2.articles.domain

data class PriceCondition(
    val conditionId: Long? = null,
    val conditionType: String,
    val description: String,
    val scope: Char,
    val evaluation: Char,
    val availability: Char,
    val numOrder: Int,
    val values: List<PriceConditionValue>
)
