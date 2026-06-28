package org.bastanchu.churiservicesv2.customers.infrastructure.persistence

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "CUSTOMERS")
class CustomerJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_seq")
    @SequenceGenerator(name = "customers_seq", sequenceName = "customers_seq", allocationSize = 1)
    @Column(name = "customer_id")
    val customerId: Long = 0,

    @Column(name = "commercial_name", nullable = false, length = 512)
    var commercialName: String = "",

    @Column(name = "social_name", nullable = false, length = 1024)
    var socialName: String = "",

    @Column(name = "vat_number", nullable = false, length = 20)
    var vatNumber: String = "",

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_address_id", nullable = false)
    var customerAddress: AddressJpaEntity? = null,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "social_address_id", nullable = false)
    var socialAddress: AddressJpaEntity? = null,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "billing_address_id")
    var billingAddress: AddressJpaEntity? = null,

    @OneToMany(mappedBy = "customer", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var delegations: MutableList<CustomerDelegationJpaEntity> = mutableListOf()
)
