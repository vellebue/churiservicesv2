package org.bastanchu.churiservicesv2.customers.application.command

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AddressCommand(
    @field:NotBlank
    @field:Size(max = 1024)
    val name: String,

    @field:NotBlank
    @field:Size(max = 1024)
    val address: String,

    @field:NotBlank
    @field:Size(max = 10)
    val postalCode: String,

    @field:NotBlank
    @field:Size(max = 50)
    val city: String,

    @field:NotBlank
    @field:Size(min = 2, max = 2)
    val country: String,

    @field:NotBlank
    @field:Size(max = 10)
    val region: String
)
