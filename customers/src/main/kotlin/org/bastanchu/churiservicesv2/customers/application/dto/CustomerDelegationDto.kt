package org.bastanchu.churiservicesv2.customers.application.dto

data class CustomerDelegationDto(
    val delegationId: Long,
    val customerId: Long,
    val orderId: Int,
    val name: String,
    val address: AddressDto,
    val billingAddress: AddressDto? = null
)
