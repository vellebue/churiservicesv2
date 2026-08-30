package org.bastanchu.churiservicesv2.articles.application.command

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreatePriceConditionCommand(
    @field:NotNull
    val subtypeId: Long,

    @field:NotNull
    val scope: Char,

    @field:NotNull
    val evaluation: Char,

    @field:NotNull
    @field:Size(max = 512)
    val description: String,

    @field:NotEmpty
    @field:Valid
    val values: List<PriceConditionValueCommand>
)
