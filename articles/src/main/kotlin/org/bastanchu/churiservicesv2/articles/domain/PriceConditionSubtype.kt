package org.bastanchu.churiservicesv2.articles.domain

data class PriceConditionSubtype(
    val subtypeId: Long? = null,
    val conditionType: String,
    val conditionSubtype: String,
    val description: String,
    val key: String
)
