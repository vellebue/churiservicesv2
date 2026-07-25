package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "PRICE_CONDITIONS")
class PriceConditionJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_price_conditions")
    @SequenceGenerator(name = "seq_price_conditions", sequenceName = "SEQ_PRICE_CONDITIONS", allocationSize = 1)
    @Column(name = "condition_id")
    val conditionId: Long = 0,

    @Column(name = "condition_type", nullable = false, length = 4)
    var conditionType: String = "",

    @Column(name = "description", nullable = false, length = 512)
    var description: String = "",

    @Column(name = "scope", nullable = false, columnDefinition = "CHAR(1)")
    var scope: Char = 'L',

    @Column(name = "evaluation", nullable = false, columnDefinition = "CHAR(1)")
    var evaluation: Char = 'D',

    @Column(name = "availability", nullable = false, columnDefinition = "CHAR(1)")
    var availability: Char = 'A',

    @Column(name = "num_order", nullable = false)
    var numOrder: Int = 0,

    @OneToMany(mappedBy = "priceCondition", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var values: MutableList<PriceConditionValueJpaEntity> = mutableListOf()
)
