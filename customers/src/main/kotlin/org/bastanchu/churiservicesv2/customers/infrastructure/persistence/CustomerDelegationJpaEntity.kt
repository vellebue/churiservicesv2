package org.bastanchu.churiservicesv2.customers.infrastructure.persistence

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "CUSTOMER_DELEGATIONS")
class CustomerDelegationJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_delegations_seq")
    @SequenceGenerator(name = "customer_delegations_seq", sequenceName = "customer_delegations_seq", allocationSize = 1)
    @Column(name = "delegation_id")
    val delegationId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    var customer: CustomerJpaEntity? = null,

    @Column(name = "order_id", nullable = false)
    var orderId: Int = 0,

    @Column(name = "name", nullable = false, length = 512)
    var name: String = "",

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", nullable = false)
    var address: AddressJpaEntity? = null,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "billing_address_id")
    var billingAddress: AddressJpaEntity? = null
)
