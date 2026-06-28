package org.bastanchu.churiservicesv2.customers.domain

data class Customer(
    val customerId: Long? = null,
    val commercialName: String,
    val socialName: String,
    val vatNumber: String,
    val customerAddress: Address,
    val socialAddress: Address,
    val billingAddress: Address? = null,
    val delegations: List<CustomerDelegation> = emptyList()
)
