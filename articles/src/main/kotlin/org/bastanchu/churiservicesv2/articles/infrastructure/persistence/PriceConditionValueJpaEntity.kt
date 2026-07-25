package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "PRICE_CONDITION_VALUES")
class PriceConditionValueJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_price_condition_values")
    @SequenceGenerator(name = "seq_price_condition_values", sequenceName = "SEQ_PRICE_CONDITION_VALUES", allocationSize = 1)
    @Column(name = "value_id")
    val valueId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id", nullable = false)
    var priceCondition: PriceConditionJpaEntity? = null,

    @Column(name = "type", nullable = false, columnDefinition = "CHAR(1)")
    var type: Char = 'A',

    @Column(name = "value", nullable = false, precision = 12, scale = 6)
    var value: BigDecimal = BigDecimal.ZERO,

    @Column(name = "begin_date", nullable = false)
    var beginDate: LocalDate = LocalDate.now(),

    @Column(name = "end_date")
    var endDate: LocalDate? = null
)
