package org.bastanchu.churiservicesv2.customers.domain

import org.bastanchu.churiservicesv2.common.domain.pagination.FilteredPage
import org.bastanchu.churiservicesv2.common.domain.pagination.PageRequest

interface CustomerDelegationRepository {
    fun save(delegation: CustomerDelegation): CustomerDelegation
    fun findById(id: Long): CustomerDelegation?
    fun deleteById(id: Long)
    fun findByFilter(
        namePattern: String?,
        pageRequest: PageRequest
    ): FilteredPage<CustomerDelegation>
    fun maxOrderIdForCustomer(customerId: Long): Int?
    fun existsByCustomerIdAndOrderId(customerId: Long, orderId: Int, excludeDelegationId: Long? = null): Boolean
}
