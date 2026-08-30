package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "C_PRICE_CONDITION_SUBTYPES",
    uniqueConstraints = [
        UniqueConstraint(
            name = "UK_PRICE_CONDITION_SUBTYPES_MNEMONIC",
            columnNames = ["condition_type", "condition_subtype"]
        )
    ]
)
class PriceConditionSubtypeJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_price_condition_subtypes")
    @SequenceGenerator(name = "seq_price_condition_subtypes", sequenceName = "SEQ_PRICE_CONDITION_SUBTYPES", allocationSize = 1)
    @Column(name = "subtype_id")
    val subtypeId: Long = 0,

    @Column(name = "condition_type", nullable = false, length = 4)
    val conditionType: String = "",

    @Column(name = "condition_subtype", nullable = false, length = 4)
    val conditionSubtype: String = "",

    @Column(name = "description", nullable = false, length = 256)
    val description: String = "",

    @Column(name = "subtype_key", nullable = false, length = 100)
    val subtypeKey: String = ""
)
