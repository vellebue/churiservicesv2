package org.bastanchu.churiservicesv2.articles.domain

import java.math.BigDecimal

data class ArticleFormat(
    val id: Long? = null,
    val description: String,
    val referenceUnit: Boolean,
    val eanCode: String?,
    val eanType: String?,
    val saleUnit: Boolean,
    val conversionFactor: BigDecimal,
    val articleUnitId: String
)
