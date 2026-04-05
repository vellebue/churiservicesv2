package org.bastanchu.churiservicesv2.articles.application.dto

import java.math.BigDecimal

data class ArticleFormatDto(
    val id: Long,
    val description: String,
    val referenceUnit: Boolean,
    val eanCode: String?,
    val eanType: String?,
    val saleUnit: Boolean,
    val conversionFactor: BigDecimal,
    val articleUnitId: String
)
