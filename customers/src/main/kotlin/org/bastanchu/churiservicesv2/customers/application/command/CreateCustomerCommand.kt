package org.bastanchu.churiservicesv2.customers.application.command

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateCustomerCommand(
    @field:NotBlank
    @field:Size(max = 512)
    val commercialName: String,

    @field:NotBlank
    @field:Size(max = 1024)
    val socialName: String,

    @field:NotBlank
    @field:Size(max = 20)
    val vatNumber: String,

    @field:NotNull
    @field:Valid
    val customerAddress: AddressCommand,

    @field:NotNull
    @field:Valid
    val socialAddress: AddressCommand,

    @field:Valid
    val billingAddress: AddressCommand? = null
)
