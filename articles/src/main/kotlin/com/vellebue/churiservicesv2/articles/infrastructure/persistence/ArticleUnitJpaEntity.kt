package com.vellebue.churiservicesv2.articles.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "C_ARTICLE_UNITS")
class ArticleUnitJpaEntity(
    @Id
    @Column(name = "symbol", length = 10)
    val symbol: String = "",

    @Column(name = "description", nullable = false, length = 100)
    val description: String = "",

    @Column(name = "translation_key", nullable = false, length = 100)
    val translationKey: String = ""
)
