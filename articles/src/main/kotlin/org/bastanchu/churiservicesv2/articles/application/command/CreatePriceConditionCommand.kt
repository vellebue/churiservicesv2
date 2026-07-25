package org.bastanchu.churiservicesv2.articles.application.command

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreatePriceConditionCommand(
    @field:NotBlank
    @field:Size(max = 4)
    val conditionType: String,

    @field:NotBlank
    @field:Size(max = 512)
    val description: String,

    @field:NotNull
    val scope: Char,

    @field:NotNull
    val evaluation: Char,

    @field:NotNull
    val availability: Char,

    @field:NotNull
    val numOrder: Int,

    @field:NotEmpty
    @field:Valid
    val values: List<PriceConditionValueCommand>
)
