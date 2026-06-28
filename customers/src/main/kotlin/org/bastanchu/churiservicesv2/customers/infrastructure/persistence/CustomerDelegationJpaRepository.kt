package org.bastanchu.churiservicesv2.customers.infrastructure.persistence

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CustomerDelegationJpaRepository : JpaRepository<CustomerDelegationJpaEntity, Long> {

    @Query(
        "SELECT d FROM CustomerDelegationJpaEntity d WHERE " +
        "(:name IS NULL OR d.name LIKE :name)"
    )
    fun findByFilter(
        @Param("name") name: String?,
        pageable: Pageable
    ): List<CustomerDelegationJpaEntity>

    @Query("SELECT MAX(d.orderId) FROM CustomerDelegationJpaEntity d WHERE d.customer.customerId = :customerId")
    fun maxOrderIdForCustomer(@Param("customerId") customerId: Long): Int?

    @Query(
        "SELECT COUNT(d) > 0 FROM CustomerDelegationJpaEntity d WHERE " +
        "d.customer.customerId = :customerId AND d.orderId = :orderId AND " +
        "(:excludeDelegationId IS NULL OR d.delegationId <> :excludeDelegationId)"
    )
    fun existsByCustomerIdAndOrderId(
        @Param("customerId") customerId: Long,
        @Param("orderId") orderId: Int,
        @Param("excludeDelegationId") excludeDelegationId: Long?
    ): Boolean
}
