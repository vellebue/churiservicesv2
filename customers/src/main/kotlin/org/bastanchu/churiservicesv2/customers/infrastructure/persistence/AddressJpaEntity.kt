package org.bastanchu.churiservicesv2.customers.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "ADDRESSES")
class AddressJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addresses_seq")
    @SequenceGenerator(name = "addresses_seq", sequenceName = "addresses_seq", allocationSize = 1)
    @Column(name = "address_id")
    val addressId: Long = 0,

    @Column(name = "address_type", nullable = false, length = 10)
    var addressType: String = "",

    @Column(name = "name", nullable = false, length = 1024)
    var name: String = "",

    @Column(name = "address", nullable = false, length = 1024)
    var address: String = "",

    @Column(name = "postal_code", nullable = false, length = 10)
    var postalCode: String = "",

    @Column(name = "city", nullable = false, length = 50)
    var city: String = "",

    @Column(name = "country", nullable = false, length = 2)
    var country: String = "",

    @Column(name = "region", nullable = false, length = 10)
    var region: String = ""
)
