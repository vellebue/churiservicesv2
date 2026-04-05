package org.bastanchu.churiservicesv2.articles.application.command

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class ArticleFormatCommand(
    @field:NotBlank
    @field:Size(max = 50)
    val description: String,

    val referenceUnit: Boolean,

    @field:Size(max = 20)
    val eanCode: String?,

    @field:Size(max = 10)
    val eanType: String?,

    val saleUnit: Boolean,

    @field:DecimalMin(value = "0", inclusive = false)
    val conversionFactor: BigDecimal,

    @field:NotBlank
    @field:Size(max = 10)
    val articleUnitId: String
)
