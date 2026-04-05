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

@Entity
@Table(name = "ARTICLE_FORMATS")
class ArticleFormatJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_formats_seq")
    @SequenceGenerator(name = "article_formats_seq", sequenceName = "article_formats_seq", allocationSize = 1)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    var article: ArticleJpaEntity? = null,

    @Column(name = "description", nullable = false, length = 50)
    val description: String = "",

    @Column(name = "reference_unit", nullable = false)
    val referenceUnit: Boolean = false,

    @Column(name = "ean_code", length = 20)
    val eanCode: String? = null,

    @Column(name = "ean_type", length = 10)
    val eanType: String? = null,

    @Column(name = "sale_unit", nullable = false)
    val saleUnit: Boolean = false,

    @Column(name = "conversion_factor", nullable = false)
    val conversionFactor: BigDecimal = BigDecimal.ZERO,

    @Column(name = "article_unit_id", nullable = false, length = 10)
    val articleUnitId: String = ""
)
