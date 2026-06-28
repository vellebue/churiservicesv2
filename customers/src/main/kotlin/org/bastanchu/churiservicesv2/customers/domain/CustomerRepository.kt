package org.bastanchu.churiservicesv2.customers.domain

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest

interface CustomerRepository {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Customer?
    fun deleteById(id: Long)
    fun findByFilter(
        commercialNamePattern: String?,
        socialNamePattern: String?,
        vatNumberPattern: String?,
        pageRequest: PageRequest
    ): FilteredPage<Customer>
    fun existsByCountryAndVatNumber(country: String, vatNumber: String, excludeCustomerId: Long? = null): Boolean
}
