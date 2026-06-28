package org.bastanchu.churiservicesv2.customers.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "ADDRESS_TYPES")
class AddressTypeJpaEntity(
    @Id
    @Column(name = "address_type", length = 10)
    val addressType: String = "",

    @Column(name = "description", nullable = false, length = 255)
    val description: String = "",

    @Column(name = "address_key", nullable = false, length = 50)
    val addressKey: String = ""
)
