package org.bastanchu.churiservicesv2.articles.domain

data class PriceCondition(
    val conditionId: Long? = null,
    val subtypeId: Long,
    val scope: Char,
    val evaluation: Char,
    val description: String,
    val values: List<PriceConditionValue> = emptyList()
)
