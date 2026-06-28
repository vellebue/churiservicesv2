package org.bastanchu.churiservicesv2.customers.infrastructure.persistence

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CustomerJpaRepository : JpaRepository<CustomerJpaEntity, Long> {

    @Query(
        "SELECT c FROM CustomerJpaEntity c WHERE " +
        "(:commercialName IS NULL OR c.commercialName LIKE :commercialName) AND " +
        "(:socialName IS NULL OR c.socialName LIKE :socialName) AND " +
        "(:vatNumber IS NULL OR c.vatNumber LIKE :vatNumber)"
    )
    fun findByFilter(
        @Param("commercialName") commercialName: String?,
        @Param("socialName") socialName: String?,
        @Param("vatNumber") vatNumber: String?,
        pageable: Pageable
    ): List<CustomerJpaEntity>

    @Query(
        "SELECT COUNT(c) > 0 FROM CustomerJpaEntity c WHERE " +
        "c.socialAddress.country = :country AND c.vatNumber = :vatNumber AND " +
        "(:excludeCustomerId IS NULL OR c.customerId <> :excludeCustomerId)"
    )
    fun existsByCountryAndVatNumber(
        @Param("country") country: String,
        @Param("vatNumber") vatNumber: String,
        @Param("excludeCustomerId") excludeCustomerId: Long?
    ): Boolean
}
