package org.bastanchu.churiservicesv2.articles.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface PriceConditionTypeJpaRepository : JpaRepository<PriceConditionTypeJpaEntity, String>
