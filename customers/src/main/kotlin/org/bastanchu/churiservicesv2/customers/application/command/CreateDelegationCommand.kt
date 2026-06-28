package org.bastanchu.churiservicesv2.customers.application.command

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateDelegationCommand(
    @field:Min(0)
    val orderId: Int? = null,

    @field:NotBlank
    @field:Size(max = 512)
    val name: String,

    @field:NotNull
    @field:Valid
    val address: AddressCommand,

    @field:Valid
    val billingAddress: AddressCommand? = null
)
