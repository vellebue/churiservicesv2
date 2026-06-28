package org.bastanchu.churiservicesv2.customers.application.dto

data class CustomerDto(
    val customerId: Long,
    val commercialName: String,
    val socialName: String,
    val vatNumber: String,
    val customerAddress: AddressDto,
    val socialAddress: AddressDto,
    val billingAddress: AddressDto? = null,
    val delegations: List<CustomerDelegationDto> = emptyList()
)
