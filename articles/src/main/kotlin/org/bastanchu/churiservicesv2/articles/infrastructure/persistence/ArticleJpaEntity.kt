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
import java.time.LocalDate

@Entity
@Table(name = "ARTICLES")
class ArticleJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "articles_seq")
    @SequenceGenerator(name = "articles_seq", sequenceName = "articles_seq", allocationSize = 1)
    val id: Long = 0,

    @Column(name = "article_id", nullable = false, unique = true, length = 10)
    var articleId: String = "",

    @Column(name = "article_name", nullable = false, length = 150)
    var articleName: String = "",

    @Column(name = "begin_validity_date", nullable = false)
    var beginValidityDate: LocalDate = LocalDate.now(),

    @Column(name = "end_validity_date")
    var endValidityDate: LocalDate? = null,

    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var formats: MutableList<ArticleFormatJpaEntity> = mutableListOf()
)
