package org.bastanchu.churiservicesv2.customers.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface AddressTypeJpaRepository : JpaRepository<AddressTypeJpaEntity, String>
