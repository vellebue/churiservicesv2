package org.bastanchu.churiservicesv2.customers.application.dto

data class CustomerFilterDto(
    val commercialName: String? = null,
    val socialName: String? = null,
    val vatNumber: String? = null
)
