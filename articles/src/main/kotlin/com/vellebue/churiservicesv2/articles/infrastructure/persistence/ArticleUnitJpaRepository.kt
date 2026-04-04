package com.vellebue.churiservicesv2.articles.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface ArticleUnitJpaRepository : JpaRepository<ArticleUnitJpaEntity, String>
