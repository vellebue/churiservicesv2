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

    @Column(name = "subtype_id", nullable = false)
    var subtypeId: Long = 0,

    @Column(name = "scope", nullable = false, length = 1)
    var scope: Char = ' ',

    @Column(name = "evaluation", nullable = false, length = 1)
    var evaluation: Char = ' ',

    @Column(name = "description", nullable = false, length = 512)
    var description: String = "",

    @OneToMany(mappedBy = "priceCondition", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var values: MutableList<PriceConditionValueJpaEntity> = mutableListOf()
)
