package org.bastanchu.churiservicesv2.customers.domain

data class Address(
    val addressId: Long? = null,
    val addressType: String,
    val name: String,
    val address: String,
    val postalCode: String,
    val city: String,
    val country: String,
    val region: String
)
