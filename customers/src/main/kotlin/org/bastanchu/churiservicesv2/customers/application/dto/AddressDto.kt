package org.bastanchu.churiservicesv2.customers.application.dto

data class AddressDto(
    val addressId: Long,
    val addressType: String,
    val name: String,
    val address: String,
    val postalCode: String,
    val city: String,
    val country: String,
    val region: String
)
