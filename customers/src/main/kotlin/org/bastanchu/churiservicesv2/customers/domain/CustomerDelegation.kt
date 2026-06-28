package org.bastanchu.churiservicesv2.customers.domain

data class CustomerDelegation(
    val delegationId: Long? = null,
    val customerId: Long,
    val orderId: Int,
    val name: String,
    val address: Address,
    val billingAddress: Address? = null
)
